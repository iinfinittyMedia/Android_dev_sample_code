package com.example.composetimer.main.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.composetimer.home.view.Home
import com.example.composetimer.main.viewmodel.MainViewModel
import com.example.composetimer.extensions.composables.ActivityBaseTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityBaseTheme {
                Home()
            }
        }
    }
}

