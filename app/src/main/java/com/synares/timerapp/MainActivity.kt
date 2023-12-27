package com.synares.timerapp

import android.graphics.Rect
import android.os.Bundle
import android.text.TextPaint
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.synares.timerapp.ui.theme.TimerAppTheme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimerAppTheme {
                // Provide lifecycleOwner for visibility and lifecycle handling
                Surface(modifier = Modifier.fillMaxSize()) {
                    TimerApp()
                }
            }
        }
    }
}

@Composable
fun TimerApp() {
    val viewModel: TimerViewModel = viewModel()
    val timerState by viewModel.timerState.collectAsState()
    val currentTime by viewModel.currentTime.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimerRing(timerState, currentTime)
        Spacer(modifier = Modifier.height(16.dp))
        TimerControls(
            timerState,
            onClickStartPause = viewModel::startPauseTimer,
            onClickStop = viewModel::stopTimer
        )
    }
}

@Composable
fun TimerRing(timerState: TimerState, currentTime: Long) {
    val textPaint = TextPaint().apply {
        color = Color.Black.hashCode()
        textSize = 36f // Adjust font size as needed
    }

    Canvas(modifier = Modifier.size(200.dp)) {
        val sweepAngle = when (timerState) {
            TimerState.RUNNING, TimerState.PAUSED -> 360f - (currentTime / 60000f * 360f)
            else -> 0f
        }
        drawArc(
            color = Color.LightGray,
            startAngle = 270f,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = 16F, cap = StrokeCap.Round)
        )

        // Center the text within the ring
        val text = formatTime(currentTime)
        val textBounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, textBounds)

        val center = Offset(size.width / 2, size.height / 2)

        // Draw the timer text directly on the canvas with center alignment
        drawContext.canvas.nativeCanvas.drawText(
            text,
            center.x - textBounds.exactCenterX(),
            center.y - textBounds.exactCenterY() + textBounds.height() / 2, // Correct vertical alignment
            textPaint
        )
    }
}

@Composable
fun TimerControls(timerState: TimerState, onClickStartPause: () -> Unit, onClickStop: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onClickStartPause,
            modifier = Modifier.weight(1f)
        ) {
            Text(if (timerState == TimerState.STOPPED || timerState == TimerState.FINISHED || timerState == TimerState.PAUSED) "Start" else "Pause")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = onClickStop,
            modifier = Modifier.weight(1f)
        ) {
            Text("Stop")
        }
    }
}

fun formatTime(milliseconds: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60
    val millisecondsFormatted = (milliseconds % 1000) / 10 // Extract two digits for milliseconds

    return String.format("%02d:%02d.%02d", minutes, seconds, millisecondsFormatted)
}