package com.example.composetimer.home.di

import com.example.composetimer.home.viewModel.HomeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    // Your definitions ...
    viewModel { HomeViewModel(androidApplication()) }
}