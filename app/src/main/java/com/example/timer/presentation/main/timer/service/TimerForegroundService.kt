package com.example.timer.presentation.main.timer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.timer.presentation.main.MainActivity
import com.example.timer.R
import com.example.timer.eventBus.EventTimeMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.greenrobot.eventbus.EventBus

/**
 * Foreground service to run the time in the background
 */
class TimerForegroundService : Service() {
    companion object {
        //constants
        const val COUNT_DOWN_TOTAL_TIME_IN_MILLI_SECONDS = 60000L // 1 minute in milli seconds
        const val COUNT_DOWN_INTERVAL_IN_MILLI_SECONDS = 10L // 10 milli second count down interval
        const val KEY_APP_FOREGROUND_STATUS = "KEY_APP_FOREGROUND_STATUS"
        const val KEY_START_TIMER = "KEY_START_TIMER"
        const val KEY_PAUSE_TIMER = "KEY_PAUSE_TIMER"
        const val KEY_STOP_TIMER = "KEY_STOP_TIMER"

        const val FOREGROUND_SERVICE_NOTIFICATION_ID =
            90199 // Unique ID for the foreground service notification
        const val COUNT_DOWN_END_NOTIFICATION_ID =
            90299 // Unique ID for the count down end notification
        const val CHANNEL_ID =
            "Timer90199" // Notification channel ID
    }

    private val _currentTime = MutableStateFlow(COUNT_DOWN_TOTAL_TIME_IN_MILLI_SECONDS)
    private var timer: CountDownTimer? = null // count down timer
    private var isTimerRunning = false // to keep record of timer if running or paused
    private var isAppInBackground =
        false //used to show end notification only if app is in background
    private var isServiceRunning =
        false //used to keep record if service notification is already sent

    /**
     * service on create method
     */
    override fun onCreate() {
        super.onCreate()
    }

    /**
     * service method to perform background tasks
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Perform background tasks here
        Log.d("TimerForegroundService", "Service started")
        if (!isServiceRunning) {
            // Create the notification for the foreground service
            val notification: Notification = createNotification(isServiceNotification = true)
            createNotificationChannel()
            // Start the service in the foreground
            startForeground(FOREGROUND_SERVICE_NOTIFICATION_ID, notification)
            isServiceRunning = true
        }
        if (intent?.hasExtra(KEY_APP_FOREGROUND_STATUS) == true) {
            //update app foreground status
            isAppInBackground = intent.getBooleanExtra(KEY_APP_FOREGROUND_STATUS, false)
        }
        val startTimer = intent?.getBooleanExtra(KEY_START_TIMER, false)
        val pauseTimer = intent?.getBooleanExtra(KEY_PAUSE_TIMER, false)
        val stopTimer = intent?.getBooleanExtra(KEY_STOP_TIMER, false)
        if (startTimer == true) {
            startTimer()
        } else if (pauseTimer == true) {
            pauseTimer()
        } else if (stopTimer == true) {
            stopTimer()
            stopForegroundService()
        }
        return START_STICKY // Restart the service if it gets terminated
    }

    /**
     * notification channel for android oreo and above
     */
    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.timer_app_notification_channel),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /**
     * service is destroyed
     */
    override fun onDestroy() {
        super.onDestroy()
        isServiceRunning = false
        stopTimer()
    }

    /**
     * create notification
     */
    private fun createNotification(
        title: String = getString(R.string.timer_count_down),
        description: String = getString(R.string.counting_down_1_minute_time_tick_tick_tick),
        isServiceNotification: Boolean = false
    ): Notification {
        // Create an intent that will be triggered if the notification is selected
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }// Create a notification for the foreground service
        if (isServiceNotification)
            return NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setSound(null)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_timer)
                .build()
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_timer)
            .build()
    }

    /**
     * Method to stop the foreground service
     */

    private fun stopForegroundService() {
        // Stop service from running in the foreground
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            stopForeground(true)
        }

        // Stop the service
        stopSelf()
    }

    /**
     * start the count down timer
     */

    private fun startTimer() {
        if (isTimerRunning) timer?.cancel()
        else {
            timer = object : CountDownTimer(
                _currentTime.value,
                COUNT_DOWN_INTERVAL_IN_MILLI_SECONDS
            ) {
                override fun onTick(millisUntilFinished: Long) {
                    _currentTime.value = millisUntilFinished
                    sendDataToViewModel(millisUntilFinished)
                }

                override fun onFinish() {
                    //when count down is finished update time, views and stop service
                    _currentTime.value = 0
                    sendDataToViewModel(0)
                    sendCountDownEndNotification()
                    stopTimer(false)
                    stopForegroundService()
                }
            }
            timer?.start()
        }
        isTimerRunning = true
    }

    /**
     * send count down end notification if app is in background
     */
    private fun sendCountDownEndNotification() {
        if (isAppInBackground) {
            val notification =
                createNotification(description = getString(R.string.one_minute_count_down_ended))
            sendNotification(notification)
        }
    }

    /**
     * send notification
     */
    private fun sendNotification(
        notification: Notification,
        notificationId: Int = COUNT_DOWN_END_NOTIFICATION_ID
    ) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(notificationId, notification)
    }

    /**
     * stop the timer
     */
    private fun stopTimer(sendData: Boolean = true) {
        isTimerRunning = false
        _currentTime.value = COUNT_DOWN_TOTAL_TIME_IN_MILLI_SECONDS
        timer?.cancel()
        if (sendData)
            sendDataToViewModel(COUNT_DOWN_TOTAL_TIME_IN_MILLI_SECONDS)
    }

    /**
     * pause the timer
     */
    private fun pauseTimer() {
        isTimerRunning = false
        timer?.cancel()
    }

    /**
     * send remaining time to observers
     */
    fun sendDataToViewModel(data: Long) {
        EventBus.getDefault().post(EventTimeMessage(data))
    }
}
