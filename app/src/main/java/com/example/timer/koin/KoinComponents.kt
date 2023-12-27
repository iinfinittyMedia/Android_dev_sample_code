package com.example.timer.koin

import com.example.timer.presentation.main.timer.viewModel.TimerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { TimerViewModel() }
}