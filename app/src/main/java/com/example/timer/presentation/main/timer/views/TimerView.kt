package com.example.timer.presentation.main.timer.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.timer.R
import com.example.timer.presentation.main.timer.viewModel.TimerViewModel
import java.util.concurrent.TimeUnit
import kotlin.math.min


/**
 * complete timer view to be called from main
 */
@Composable
fun TimerView(timerViewModel: TimerViewModel) {
    val currentTime by timerViewModel.currentTime.collectAsState()
    val currentState by timerViewModel.currentState.collectAsState()

    val seconds = TimeUnit.MILLISECONDS.toSeconds(currentTime)
    val milliseconds = currentTime - TimeUnit.SECONDS.toMillis(seconds)
    val millisText = if (milliseconds == 0L)
        "000"
    else
        milliseconds
    val progress = seconds / 60f
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //box to contain circular progress and remaining time
        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.Center
        ) {
            GradientCircularProgressIndicator(
                modifier = Modifier.size(200.dp),
                progress = progress,
                6,
            )
            Text("${seconds}.$millisText")
        }
        Spacer(modifier = Modifier.size(100.dp))

        //row for start/pause and stop button
        Row {
            //start/pause button
            OutlinedButton(
                onClick = {
                    //call stop timer of view model when clicked
                    timerViewModel.startTimer()
                },
                modifier = Modifier.size(50.dp),  //avoid the oval shape
                shape = CircleShape,
                border = BorderStroke(1.dp, Color.Blue),
                contentPadding = PaddingValues(0.dp),  //avoid the little icon
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Blue)
            ) {
                Text(text = if (currentState) stringResource(R.string.pause) else stringResource(R.string.start))
            }
            Spacer(modifier = Modifier.size(100.dp))
            //stop button
            OutlinedButton(
                onClick = {
                    //call stop timer of view model when clicked
                    timerViewModel.stopTimer()
                },
                modifier = Modifier.size(50.dp),  //avoid the oval shape
                shape = CircleShape,
                border = BorderStroke(1.dp, Color.Red),
                contentPadding = PaddingValues(0.dp),  //avoid the little icon
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Text(text = stringResource(R.string.stop))
            }
        }
    }
}

/**
 * Custom circular progress bar to show different colors
 */
@Composable
fun GradientCircularProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    totalSegments: Int = 10,
    strokeWidth: Dp = 8.dp
) {
    Canvas(
        modifier = modifier
    ) {
        val segmentAngle = 360f / totalSegments

        for (i in 0 until totalSegments) {
            val startAngle = i * segmentAngle - 90
            val endAngle = startAngle + segmentAngle

            val gradient = Brush.sweepGradient(
                colors = listOf(
                    Color.Red,
                    Color.Yellow,
                    Color.Blue,
                    Color.Green,
                    Color.Green,
                    // Adjust your gradient colors here
                ),
                center = Offset(startAngle, endAngle)
            )

            val segmentProgress = min(1f, progress - (i.toFloat() / totalSegments))
            if (segmentProgress > 0) {
                drawArc(
                    brush = gradient,
                    startAngle = startAngle,
                    sweepAngle = min(segmentProgress * 360, segmentAngle),
                    useCenter = false,
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                )
            }
        }
    }
}
