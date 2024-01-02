package com.example.composetimer.main.di

import com.example.composetimer.main.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    // Your definitions ...
    viewModel { MainViewModel(androidApplication()) }
}