package com.example.composetimer.home.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.composetimer.extensions.composables.ActivityBaseTheme
import com.example.composetimer.extensions.composables.CButton
import com.example.composetimer.extensions.composables.CText
import com.example.composetimer.extensions.composables.CTextField

@Composable
fun Home() = ConstraintLayout(
    modifier = Modifier
        .fillMaxSize()
        .layoutId("ParentLayout")
) {

    //Inject ViewModel
//    val viewModel = koinViewModel<HomeViewModel>()

    var timerTextState by remember { mutableStateOf("0.0.0") }

    // Create references for the composable to constrain
    val (heading, timerBox, progressBar, startPauseButton, stopButton) = createRefs()

    Greeting(
        name = "Android",
        modifier = Modifier.constrainAs(heading) {
            centerHorizontallyTo(parent)
        }
    )

//    CText(
//        text = "0:0:0",
//        color = Color.Blue,
//        fontSize = 34.sp,
//        modifier = Modifier.constrainAs(timerBox) {
//            centerHorizontallyTo(parent)
//            centerVerticallyTo(parent)
//        }
//    )

    Box(
        modifier = Modifier
            .width(75.dp)
            .constrainAs(timerBox) {
                centerHorizontallyTo(parent)
                centerVerticallyTo(parent)
            }
    ) {
        CTextField(
            value = timerTextState,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            )
        )
    }

    CircularProgressIndicator(
        progress = 0.4f,
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .width(104.dp)
            .constrainAs(progressBar) {
                top.linkTo(timerBox.top)
                start.linkTo(timerBox.start)
                end.linkTo(timerBox.end)
                bottom.linkTo(timerBox.bottom)
            }
            .padding(bottom = 60.dp)
    )

    CButton(
        text = "Start/Pause",
        color = Color.White,
        modifier = Modifier
            .constrainAs(startPauseButton) {
                top.linkTo(progressBar.bottom)
                start.linkTo(timerBox.start)
                end.linkTo(timerBox.end)
            }
            .padding(top = 20.dp)
    ) {
        Log.d("TAG", "abc")         //OnClick
    }

    CButton(
        text = "Stop",
        color = Color.White,
        modifier = Modifier
            .constrainAs(stopButton) {
                top.linkTo(startPauseButton.bottom)
                start.linkTo(timerBox.start)
                end.linkTo(timerBox.end)
            }
    ) {
        Log.d("TAG", "efg")         //OnClick
        timerTextState = "0.0.0"
    }
}

@Composable
private fun Greeting(name: String, modifier: Modifier = Modifier) = CText(
    text = "Hello $name!",
    modifier = modifier
)

@Preview(showBackground = true)
@Composable
private fun Preview() = ActivityBaseTheme {
    Home()
}