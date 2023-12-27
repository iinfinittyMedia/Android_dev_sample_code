package com.example.timer.presentation.main.timer.viewModel

import androidx.lifecycle.ViewModel
import com.example.timer.eventBus.EventTimeMessage
import com.example.timer.enumerations.TimerAction
import com.example.timer.presentation.main.timer.service.TimerForegroundService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class TimerViewModel : ViewModel() {
    init {
        //register events bus to receive time updates
        EventBus.getDefault().register(this)
    }

    //keep record of the remaining time
    private val _currentTime =
        MutableStateFlow(TimerForegroundService.COUNT_DOWN_TOTAL_TIME_IN_MILLI_SECONDS)
    val currentTime: StateFlow<Long> = _currentTime

    //action to perform when start/pause/stop button is pressed or time ends
    private val _actionToPerform =
        MutableStateFlow(TimerAction.FINISHED)
    val actionToPerform: StateFlow<TimerAction> = _actionToPerform

    //current state of the timer
    var currentState = MutableStateFlow(false)

    /**
     * called when Start/Pause button is pressed
     */
    fun startTimer() {
        if (currentState.value)
            _actionToPerform.value = TimerAction.PAUSE_TIMER
        else
            _actionToPerform.value = TimerAction.START_TIMER
        currentState.value = !currentState.value
    }

    /**
     * called when Stop button is pressed
     */
    fun stopTimer() {
        if (currentState.value)
            _actionToPerform.value = TimerAction.STOP_TIMER
        currentState.value = false
    }

    override fun onCleared() {
        super.onCleared()
        //unregister event bus
        EventBus.getDefault().unregister(this)
    }

    /**
     * time updates from service will be received here
     */
    @Subscribe
    fun onMessageEvent(event: EventTimeMessage) {
        _currentTime.value = event.remainingTime
        //update the state if timer ends
        if (event.remainingTime == 0L) {
            _actionToPerform.value = TimerAction.FINISHED
            currentState.value = false
        }
    }
}