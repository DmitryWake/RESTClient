package com.ewake.restclient.presentation.viewmodel.scriptdetail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ewake.restclient.data.db.room.AppDatabase
import com.ewake.restclient.data.db.room.entity.RequestResponseEntity
import com.ewake.restclient.data.db.room.mapper.RequestResponseMapper
import com.ewake.restclient.presentation.extensions.toLiveData
import com.ewake.restclient.presentation.model.RequestModel
import com.ewake.restclient.presentation.model.RequestResponseModel
import com.ewake.restclient.presentation.model.ResponseModel
import com.ewake.restclient.presentation.utils.SingleEventLiveData
import com.ewake.restclient.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class ScriptDetailViewModel @Inject constructor(
    application: Application,
    appDatabase: AppDatabase,
    private val mapper: RequestResponseMapper,
    private val httpClient: OkHttpClient
) : BaseViewModel(application) {

    private val _listLiveData = MutableLiveData<MutableList<RequestResponseModel>>()
    val listLiveData = _listLiveData.toLiveData()

    private val _deleteItemLiveData = SingleEventLiveData<Int>()
    val deleteItemLiveData = _deleteItemLiveData.toLiveData()

    private val _addLiveData = SingleEventLiveData<RequestResponseModel>()
    val addLiveData = _addLiveData.toLiveData()

    private val dao = appDatabase.requestResponseDao()

    private var list = mutableListOf<RequestResponseModel>()

    var scriptId: Int? = null

    override fun onStart() {
        super.onStart()
        viewModelScope.launch {
            scriptId?.let { loadList(it) }
        }
    }

    private suspend fun loadList(id: Int) {
        list = dao.getFromScript(id).map { mapper.entityToModel(it) }.toMutableList()

        _listLiveData.postValue(list)
    }

    fun onAddClicked() {
        viewModelScope.launch {
            dao.insert(RequestResponseEntity(scriptId = scriptId))
            val model = mapper.entityToModel(dao.getAll().last())
            list.add(model)
            _addLiveData.postValue(model)
        }
    }

    fun onDeleteClicked(currentItem: Int) {
        viewModelScope.launch {
            dao.delete(mapper.modelToEntity(list[currentItem]))
            list.removeAt(currentItem)
            _deleteItemLiveData.postValue(currentItem)
        }
    }

    fun onSaveClicked(items: List<RequestResponseModel>) {
        items.forEach { item ->
            if (list.find { it.id == item.id } != null) {
                viewModelScope.launch { dao.update(mapper.modelToEntity(item)) }
            } else {
                viewModelScope.launch { dao.insert(mapper.modelToEntity(item)) }
            }
        }
    }

    fun onSendRequestClicked(id: Int, requestModel: RequestModel) {
        viewModelScope.launch(Dispatchers.IO) {
            send(id, requestModel)
        }
    }

    private suspend fun send(id: Int, requestModel: RequestModel) {
        try {
            val builder = Request.Builder()

            builder.method(
                requestModel.method.name,
                if (requestModel.body.isNotBlank()) {
                    requestModel.body.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                } else {
                    null
                }
            )

            requestModel.headers.forEach {
                builder.addHeader(it.first, it.second)
            }

            val urlBuilder = requestModel.url.toHttpUrlOrNull()?.newBuilder()

            if (urlBuilder == null) {
                _messageLiveData.postValue("Не удалось распарсить url")
                return
            }

            requestModel.query.forEach {
                urlBuilder.addQueryParameter(it.first, it.second)
            }

            builder.url(urlBuilder.build())

            val request = builder.build()

            kotlin.runCatching {
                httpClient.newCall(request).execute()
            }.onSuccess { response ->
                val responseModel =
                    ResponseModel(response.code, response.message, response.body?.string() ?: "")
                list.find { it.id == id }?.response = responseModel
                _listLiveData.postValue(list)
                list.find { it.id == id }?.let {
                    dao.update(mapper.modelToEntity(it))
                }
            }.onFailure {
                _messageLiveData.postValue(it.message)
            }
        } catch (t: Throwable) {
            _messageLiveData.postValue(t.message)
        }
    }
}