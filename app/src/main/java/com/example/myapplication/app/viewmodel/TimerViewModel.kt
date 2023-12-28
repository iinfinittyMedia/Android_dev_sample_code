package com.example.myapplication.app.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.app.constants.DEFAULT_TIME
import com.example.myapplication.app.constants.UPDATE_INTERVAL
import com.example.myapplication.app.utils.TimerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class TimerViewModel : ViewModel()  {

    private val _timerState = MutableStateFlow(TimerState.STOPPED)
    val timerState: StateFlow<TimerState> = _timerState

    private val _currentTime = MutableStateFlow(DEFAULT_TIME)
    val currentTime: StateFlow<Long> = _currentTime

    private var timerJob: kotlinx.coroutines.Job? = null
    init {
        _timerState.value = TimerState.STOPPED
        _currentTime.value = DEFAULT_TIME
    }

    fun startPauseTimer() {
        when (_timerState.value) {
            TimerState.STOPPED -> startTimer()
            TimerState.RUNNING -> pauseTimer()
            TimerState.PAUSED -> resumeTimer()
            else -> {}
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        _timerState.value = TimerState.PAUSED
        _currentTime.value = DEFAULT_TIME
    }

     fun startTimer() {
        timerJob = viewModelScope.launch {
            _timerState.value = TimerState.RUNNING

            while (_currentTime.value > 0) {
                delay(UPDATE_INTERVAL)
                _currentTime.value -= UPDATE_INTERVAL
            }
            _timerState.value = TimerState.FINISHED
        }
    }

     fun pauseTimer() {
        timerJob?.cancel()
        _timerState.value = TimerState.PAUSED
    }

     fun resumeTimer() {
        startTimer()
    }


}
