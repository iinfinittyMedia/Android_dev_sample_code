package com.example.myapplication.app.view

import NotificationServices
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.app.component.CustomIconButton
import com.example.myapplication.app.constants.DEFAULT_TIME
import com.example.myapplication.app.utils.TimerState
import com.example.myapplication.app.utils.formatTime
import com.example.myapplication.app.viewmodel.TimerViewModel
@Composable
fun TimerScreen(viewModel: TimerViewModel) {
    var currentTime by remember { mutableStateOf(viewModel.currentTime.value) }
    var timerState by remember { mutableStateOf(viewModel.timerState.value) }
    val notificationService = NotificationServices(LocalContext.current)
    LaunchedEffect(viewModel.currentTime) {
        viewModel.currentTime.collect { newTime ->
            currentTime = newTime
        }
    }

    LaunchedEffect(viewModel.timerState) {
        viewModel.timerState.collect { newState ->
            timerState = newState
        }

    }

    if (timerState == TimerState.FINISHED) {
        notificationService.showNotification()
        viewModel.stopTimer()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(Color.Gray)

        ) {
            CircularProgressIndicator(
                progress = when (timerState) {
                    TimerState.STOPPED -> 1f
                    else -> (currentTime.toFloat() / DEFAULT_TIME.toFloat())
                },
                modifier = Modifier
                    .size(200.dp),
                strokeWidth = 8.dp,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = formatTime(currentTime),

                color = Color.White,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomEnd)
                    .padding(top = 80.dp),

                textAlign = TextAlign.Center

            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            CustomIconButton(
                onClick = { viewModel.startPauseTimer() },
                icon = when (timerState) {
                    TimerState.RUNNING -> Icons.Default.Pause
                    else -> Icons.Default.PlayArrow
                },
                backgroundColor = when (timerState) {
                    TimerState.RUNNING -> Color.Gray
                    else -> MaterialTheme.colorScheme.primary
                }
            )
            CustomIconButton(
                onClick = { viewModel.stopTimer() },
                icon = Icons.Default.Refresh,
                backgroundColor = when (timerState) {
                    TimerState.RUNNING -> Color.Gray
                    else -> MaterialTheme.colorScheme.primary
                }
            )
        }

    }
}



