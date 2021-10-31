package com.ewake.walkinghealth.presentation.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * @author Nikolaevsky Dmitry (@d.nikolaevskiy)
 */
@HiltAndroidApp
class App @Inject constructor() : Application() {
}