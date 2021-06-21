package com.example.storagedatabaseapp

import android.app.Application
import com.example.storagedatabaseapp.di.DaggerAppComponent
import com.example.storagedatabaseapp.di.DatabaseModule

class MyApplication : Application() {

    lateinit var appComponent: DaggerAppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .databaseModule(DatabaseModule(applicationContext))
            .build() as DaggerAppComponent
    }
}