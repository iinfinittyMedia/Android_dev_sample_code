package com.example.asiftasknexrupt

import android.app.Application
import com.example.asiftasknexrupt.viewmodels.MainActivityViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@MyApp)
            modules(myModule)
        }
    }

    private val myModule = module {
        viewModelOf(::MainActivityViewModel)
    }
}