package com.example.stopwatch.view.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stopwatch.R
import com.example.stopwatch.ViewModel.MainViewModel
import com.example.stopwatch.utility.Utility
import com.example.stopwatch.utility.Utility.formatTime

@Composable
fun CountDownView(viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    val time by viewModel.time.observeAsState(Utility.TIME_COUNTDOWN.formatTime())
    val progress by viewModel.progress.observeAsState(1.00F)
    val isPlaying by viewModel.isPlaying.observeAsState(false)
    val celebrate by viewModel.celebrate.observeAsState(false)

    CountDownView(time = time, progress = progress, isPlaying = isPlaying, celebrate = celebrate)

}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CountDownView(
    time: String,
    progress: Float,
    isPlaying: Boolean,
    celebrate: Boolean,
    viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.primary)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (celebrate) {
                ShowCelebration()
            }

            Text(
                text = "Timer",
                color = androidx.compose.ui.graphics.Color.White,
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                fontFamily = FontFamily(Font(R.font.poppins_semibold))
            )
            Text(
                text = "1 minute to launch...",
                color = androidx.compose.ui.graphics.Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                fontFamily = FontFamily(Font(R.font.poppins_semibold))
            )
            CountDownIndicator(
                Modifier.padding(top = 50.dp),
                progress = progress,
                time = time,
                size = 250,
                stroke = 12
            )
            CountDownButton(
                isPlaying = isPlaying,
                title = "START",
                onClick = { viewModel.startTimer() }
            )
            CountDownButton(
                isPlaying = isPlaying,
                title = "STOP",
                onClick = { viewModel.pauseTimer() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreCount() {
    CountDownView()
}