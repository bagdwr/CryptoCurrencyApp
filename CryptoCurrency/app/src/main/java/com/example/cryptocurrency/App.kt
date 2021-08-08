package com.example.cryptocurrency

import android.app.Application
import com.example.cryptocurrency.di.AppComponent
import com.example.cryptocurrency.di.DaggerAppComponent

class App:Application() {
    val appComponent:AppComponent= DaggerAppComponent.create()
    override fun onCreate() {
        super.onCreate()
    }
}