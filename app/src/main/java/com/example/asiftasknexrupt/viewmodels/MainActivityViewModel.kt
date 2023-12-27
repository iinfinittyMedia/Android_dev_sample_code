package com.example.asiftasknexrupt.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivityViewModel : ViewModel() {

    var hasFinished: MutableLiveData<Boolean> = MutableLiveData(false)
    var totalPeriod = 60000L
    var currentProgress: Long = totalPeriod
    var isTimerPaused = true

    val timerFlow = MutableStateFlow(totalPeriod)
//    suspend fun stopTimer() {
//        timerFlow.emit(0)
//    }

    suspend fun startOrResumeTimer(start: Long, end: Long = 0L) {
        if (!isTimerPaused) {
            isTimerPaused = true
        } else {
            if (start > 0) {
                for (i in start downTo end) {
                    currentProgress = i
                    timerFlow.emit(i)
                    delay(1)
                }
                hasFinished.postValue(true)
            }
        }
    }
}