package com.ewake.restclient.presentation.viewmodel.scriptdetail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ewake.restclient.data.db.room.AppDatabase
import com.ewake.restclient.data.db.room.entity.RequestResponseEntity
import com.ewake.restclient.data.db.room.mapper.RequestResponseMapper
import com.ewake.restclient.presentation.extensions.toLiveData
import com.ewake.restclient.presentation.model.RequestResponseModel
import com.ewake.restclient.presentation.utils.SingleEventLiveData
import com.ewake.restclient.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScriptDetailViewModel @Inject constructor(
    application: Application,
    appDatabase: AppDatabase,
    private val mapper: RequestResponseMapper
): BaseViewModel(application) {

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
}