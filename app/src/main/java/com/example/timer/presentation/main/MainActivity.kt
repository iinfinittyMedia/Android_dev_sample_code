package com.example.timer.presentation.main

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.timer.ui.theme.TimerTheme
import com.example.timer.presentation.main.timer.service.TimerForegroundService
import com.example.timer.enumerations.TimerAction
import com.example.timer.presentation.main.timer.views.TimerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.provider.Settings
import com.example.timer.presentation.main.timer.viewModel.TimerViewModel

class MainActivity : ComponentActivity() {
    private val timerViewModel by viewModel<TimerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TimerView(timerViewModel)
                }
            }
        }
        startCollectingData()
    }

    override fun onStart() {
        super.onStart()
        updateAppForegroundStatusInService()
    }

    override fun onResume() {
        super.onResume()
        //check notification permission
        val isNotificationEnabled = areNotificationsEnabled(this)
        if (isNotificationEnabled) {
            // Notifications are enabled
        } else {
            // Notifications are not enabled
            //ask to enable notifications
            openNotificationSettings()
        }

    }

    override fun onStop() {
        super.onStop()
        updateAppForegroundStatusInService(true)
    }

    private fun areNotificationsEnabled(context: Context): Boolean {
        //check notifications permission for android 14
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // Get all notification channels for the app
            val channels = notificationManager.notificationChannels
            // Check if there are any notification channels
            return channels.isNotEmpty()
        }
        //for android below 14 notifications permission not required
        return true
    }

    /**
     * open app notification settings for android 14
     */
    private fun openNotificationSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val intent =
                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)

            // Check if the intent can be resolved before starting the activity
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                // Handle case where app settings cannot be opened
                // You can show a message to the user indicating this
            }
        }
    }

    /**
     * observe events from view model
     */
    private fun startCollectingData() {
        lifecycleScope.launchWhenStarted {
            timerViewModel.actionToPerform.collect { action ->
                when (action) {
                    TimerAction.START_TIMER -> startTimerService()
                    TimerAction.PAUSE_TIMER -> pauseTimerService()
                    TimerAction.STOP_TIMER -> stopTimerService()
                    TimerAction.FINISHED -> {
                        //No need to do anything here for now when the count down is finished
                    }
                }
            }
        }
    }

    /**
     * notify service if app is in foreground or background
     */
    private fun updateAppForegroundStatusInService(status: Boolean = false) {
        //default is false because by default app will be in foreground when countdown starts
        val serviceIntent = Intent(this, TimerForegroundService::class.java)
        serviceIntent.putExtra(TimerForegroundService.KEY_APP_FOREGROUND_STATUS, status)
        if (timerViewModel.currentState.value) {
            //only update foreground state if timer is running
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
            } else {
                startService(serviceIntent)
            }
        }
    }

    /**
     * start timer service
     */
    private fun startTimerService() {
        val serviceIntent = Intent(this, TimerForegroundService::class.java)
        serviceIntent.putExtra(TimerForegroundService.KEY_START_TIMER, true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    /**
     * start timer in service
     */
    private fun pauseTimerService() {
        val serviceIntent = Intent(this, TimerForegroundService::class.java)
        serviceIntent.putExtra(TimerForegroundService.KEY_PAUSE_TIMER, true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    /**
     * stop the service
     */
    private fun stopTimerService() {
        val serviceIntent = Intent(this, TimerForegroundService::class.java)
        serviceIntent.putExtra(TimerForegroundService.KEY_STOP_TIMER, true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
        stopService(serviceIntent)
    }
}