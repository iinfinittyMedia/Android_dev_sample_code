package com.example.composetimer.application

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import com.example.composetimer.application.di.koinAppModules

class TimerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            allowOverride(true)
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@TimerApplication)
            // Load modules
            modules(koinAppModules)
        }
    }
}