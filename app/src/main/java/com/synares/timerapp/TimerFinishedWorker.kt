package com.synares.timerapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class TimerFinishedWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "timer_channel"
        val channel = NotificationChannel(
            channelId,
            "Timer Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Timer Finished")
            .setContentText("Your timer has completed.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        notificationManager.notify(1, notification)
        return Result.success()
    }
}