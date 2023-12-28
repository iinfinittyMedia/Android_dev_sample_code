package com.example.myapplication.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.example.myapplication.app.constants.NOTIFICATION_CHANNEL_ID
import com.example.myapplication.app.constants.NOTIFICATION_CHANNEL_NAME
import com.example.myapplication.app.utils.ScreenOffReceiver

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        val screenOffReceiver = ScreenOffReceiver()
        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        registerReceiver(screenOffReceiver, filter)
        val notificationManager:NotificationManager=
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel= NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH

        )
        notificationManager.createNotificationChannel(notificationChannel)
    }
}