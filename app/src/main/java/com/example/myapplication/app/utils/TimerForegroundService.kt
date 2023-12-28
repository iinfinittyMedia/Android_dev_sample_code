package com.example.myapplication.app.utils

import NotificationServices
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.edit
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ScreenOffReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_SCREEN_OFF) {
            val sharedPreferences = context?.getSharedPreferences("TimerPrefs", Context.MODE_PRIVATE)

            if (sharedPreferences?.getBoolean("timerCompleted", false) == false) {
                GlobalScope.launch {
                    sharedPreferences.edit {
                        putBoolean("timerCompleted", true)
                    }

                    NotificationServices(context).showNotification()
                }
            }
        }
    }
}