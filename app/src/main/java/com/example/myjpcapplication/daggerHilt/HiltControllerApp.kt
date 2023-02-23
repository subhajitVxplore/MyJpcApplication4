package com.example.myjpcapplication.daggerHilt

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HiltControllerApp:Application() {

    override fun onCreate() {
        super.onCreate()
    }
}