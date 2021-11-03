package com.ewake.restclient.presentation.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * @author Nikolaevsky Dmitry (@d.nikolaevskiy)
 */
fun <T> MutableLiveData<T>.toLiveData(): LiveData<T> = this