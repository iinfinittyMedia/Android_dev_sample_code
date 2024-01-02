package com.example.composetimer.application.di

import com.example.composetimer.home.di.homeModule
import com.example.composetimer.main.di.mainModule
import org.koin.dsl.module

val parentModule = module {
    includes(mainModule, homeModule)
}

val koinAppModules = parentModule