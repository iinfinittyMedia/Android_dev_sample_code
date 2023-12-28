package com.example.myapplication.app.utils

fun formatTime(timeInMillis: Long): String {
    val seconds = (timeInMillis / 1000) % 60
    val milliseconds = (timeInMillis % 1000) / 10
    return String.format("%02d:%02d", seconds, milliseconds)
}
