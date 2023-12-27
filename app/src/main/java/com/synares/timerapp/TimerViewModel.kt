package com.synares.timerapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin

class TimerViewModel : ViewModel() {
    private val _timerState = MutableStateFlow(TimerState.STOPPED)
    val timerState: StateFlow<TimerState> get() = _timerState

    private val _currentTime = MutableStateFlow(60_000L)
    val currentTime: StateFlow<Long> get() = _currentTime

    private var timerJob: Job? = null

    private val timerFinishedWorkRequest = OneTimeWorkRequestBuilder<TimerFinishedWorker>()
        .build()

    init {
        _timerState.value = TimerState.STOPPED
    }

    fun startPauseTimer() {
        when (_timerState.value) {
            TimerState.STOPPED -> startTimer()
            TimerState.RUNNING -> pauseTimer()
            TimerState.PAUSED -> resumeTimer()
            TimerState.FINISHED -> {}
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            _timerState.value = TimerState.RUNNING

            while (_currentTime.value > 0) {
                delay(10)
                _currentTime.value -= 10
            }
            _timerState.value = TimerState.STOPPED
            WorkManager.getInstance(getKoin().get<Context>()).enqueue(timerFinishedWorkRequest)
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
        _timerState.value = TimerState.PAUSED
    }

    private fun resumeTimer() {
        startTimer()
    }

    fun stopTimer() {
        timerJob?.cancel()
        _timerState.value = TimerState.STOPPED
        _currentTime.value = 60_000L
    }

}

enum class TimerState {
    STOPPED,
    RUNNING,
    PAUSED,
    FINISHED
}