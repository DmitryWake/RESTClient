package com.ewake.restclient.presentation.viewmodel.scriptlist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ewake.restclient.data.db.room.AppDatabase
import com.ewake.restclient.data.db.room.entity.ScriptEntity
import com.ewake.restclient.presentation.extensions.toLiveData
import com.ewake.restclient.presentation.ui.fragment.scriptlist.ScriptListFragmentDirections
import com.ewake.restclient.presentation.utils.SingleEventLiveData
import com.ewake.restclient.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScriptListViewModel @Inject constructor(
    application: Application,
    appDatabase: AppDatabase
): BaseViewModel(application) {

    private val dao = appDatabase.scriptDao()

    private val _scriptListLiveData = MutableLiveData<MutableList<ScriptEntity>>()
    val scriptListLiveData = _scriptListLiveData.toLiveData()

    private val _deleteItemLiveData = SingleEventLiveData<ScriptEntity>()
    val deleteItemLiveData = _deleteItemLiveData.toLiveData()

    private val _onItemAddLiveData = SingleEventLiveData<ScriptEntity>()
    val onItemAddLiveData = _onItemAddLiveData.toLiveData()

    private var data: MutableList<ScriptEntity> = mutableListOf()

    private suspend fun loadData() {
        data = dao.getAll().toMutableList()
        _scriptListLiveData.postValue(data)
    }

    private suspend fun deleteItem(scriptModel: ScriptEntity) {
        dao.delete(scriptModel)
        data.remove(scriptModel)
        _deleteItemLiveData.postValue(scriptModel)
    }

    fun onItemClicked(scriptModel: ScriptEntity) {
        _navigationLiveData.postValue(ScriptListFragmentDirections.actionScriptListFragmentToScriptDetailFragment(scriptModel.id!!))
    }

    fun onDeleteItemClicked(scriptModel: ScriptEntity) {
        viewModelScope.launch {
            deleteItem(scriptModel)
        }
    }

    fun onAddButtonClicked() {
        viewModelScope.launch {
            dao.insert(ScriptEntity(name = "?????????? ????????????????"))
            data = dao.getAll().toMutableList()
            _onItemAddLiveData.postValue(data.last())
        }
    }

    fun refresh() {
        viewModelScope.launch {
            loadData()
        }
    }
}