package com.example.projectakhir

import android.app.Application
import com.example.projectakhir.di.AppContainer

class FocusSenseApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
