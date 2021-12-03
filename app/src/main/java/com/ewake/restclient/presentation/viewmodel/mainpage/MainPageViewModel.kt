package com.ewake.restclient.presentation.viewmodel.mainpage

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ewake.restclient.data.db.room.AppDatabase
import com.ewake.restclient.data.db.room.mapper.RequestResponseMapper
import com.ewake.restclient.presentation.extensions.toLiveData
import com.ewake.restclient.presentation.model.RequestModel
import com.ewake.restclient.presentation.model.RequestResponseModel
import com.ewake.restclient.presentation.model.ResponseModel
import com.ewake.restclient.presentation.viewmodel.base.BaseViewModel
import com.ewake.walkinghealth.presentation.app.App
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

/**
 * @author Nikolaevsky Dmitry (@d.nikolaevskiy)
 */
@HiltViewModel
class MainPageViewModel @Inject constructor(
    app: Application,
    database: AppDatabase,
    private val httpClient: OkHttpClient,
    private val mapper: RequestResponseMapper
) : BaseViewModel(app) {

    private val _currentRequestResponseLiveData = MutableLiveData<RequestResponseModel>()
    val currentRequestResponseLiveData = _currentRequestResponseLiveData.toLiveData()

    private val _responseLiveData = MutableLiveData<ResponseModel>()
    val responseLiveData = _responseLiveData.toLiveData()

    private val _requestsLiveData = MutableLiveData<List<RequestResponseModel>>()
    val requestsLiveData = _requestsLiveData.toLiveData()

    private val requestResponseDao = database.requestResponseDao()

    override fun onStart() {
        viewModelScope.launch {
            loadData()
        }
    }

    fun onSendClicked(requestModel: RequestModel) {
        viewModelScope.launch(Dispatchers.IO) {
            send(requestModel)
        }
    }

    fun onItemClicked(requestResponseModel: RequestResponseModel) {
        _currentRequestResponseLiveData.postValue(requestResponseModel)
    }

    private suspend fun saveModel(requestResponseModel: RequestResponseModel) {
        requestResponseDao.insert(mapper.modelToEntity(requestResponseModel))
    }

    private suspend fun loadData() {
        val list = mapper.entityListToModelList(requestResponseDao.getAll())

        _requestsLiveData.postValue(list)
    }

    private suspend fun send(requestModel: RequestModel) {
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
            }.onSuccess {
                val responseModel = ResponseModel(it.code, it.message, it.body?.string() ?: "")
                _responseLiveData.postValue(responseModel)

                saveModel(RequestResponseModel(request = requestModel, response = responseModel))
                loadData()
            }.onFailure {
                _messageLiveData.postValue(it.message)
            }
        } catch (t: Throwable) {
            _messageLiveData.postValue(t.message)
        }
    }
}