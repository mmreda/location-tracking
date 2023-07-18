package com.mmreda.locationtracking

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LocationTrackingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}