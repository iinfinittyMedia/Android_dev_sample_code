package com.example.asiftasknexrupt

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.asiftasknexrupt.ui.theme.AsifTaskNexruptTheme
import com.example.asiftasknexrupt.utils.Utils
import com.example.asiftasknexrupt.viewmodels.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //Use Handler to run even If app is in background
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                //Use observerForever to observe irrespective of lifecycle
                mainActivityViewModel.hasFinished.observeForever {
                    if (it)
                        Utils.showNotification(this@MainActivity)
                }
            }
            AsifTaskNexruptTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(mainActivityViewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(mainActivityViewModel: MainActivityViewModel) {
    Column(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
            .clickable(onClick = { })
            .clip(shape = RoundedCornerShape(16.dp)),
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 4.dp,
                    color = Gray,
                    shape = RoundedCornerShape(16.dp)
                )
                .wrapContentSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val timerState = mainActivityViewModel.timerFlow.collectAsState()
                val seconds = timerState.value / 1000
                val milliSeconds = timerState.value % 1000
                CircularProgressIndicator(
                    (seconds.toFloat() * 1000 /
                            mainActivityViewModel.totalPeriod.toFloat()),
                    modifier = Modifier.padding(10.dp),
                    color = Color.Green,
                )
                Text(
                    text = "$seconds seconds : $milliSeconds milliSeconds",
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                )
            }
        }
        var job: Job by remember { mutableStateOf(Job(null)) }
        SimpleButton("start/resume") {
            job = CoroutineScope(Dispatchers.Default).launch {
                mainActivityViewModel.startOrResumeTimer(mainActivityViewModel.currentProgress)
            }
        }
        SimpleButton("pause") {
            job.cancel()
        }
    }
}

@Composable
fun SimpleButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.padding(top = 20.dp)) {
        Text(text = text)
    }
}
