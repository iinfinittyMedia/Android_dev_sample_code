package com.example.timer.application

import android.app.Application
import com.example.timer.koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class TimerApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TimerApp)
            modules(appModule)
        }
    }
}