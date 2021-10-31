package com.ewake.restclient.presentation.viewmodel.base

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.ewake.walkinghealth.presentation.app.App

/**
 * @author Nikolaevsky Dmitry (@d.nikolaevskiy)
 */
open class BaseViewModel(app: App) : AndroidViewModel(app) {

    protected val _messageLiveData = MutableLiveData<String>()
    val messageLiveData: LiveData<String> = _messageLiveData

    protected val _navigationLiveData = MutableLiveData<NavDirections>()
    val navigationLiveData: LiveData<NavDirections> = _navigationLiveData

    protected val _navigateBackLiveData = MutableLiveData<Unit>()
    val navigateBackLiveData: LiveData<Unit> = _navigateBackLiveData

    private var isStart = false

    fun start() {
        if (!isStart) {
            onStart()
        }
    }

    open fun onStart() {
        // NO IMPL
    }
}