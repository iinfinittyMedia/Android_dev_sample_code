package com.example.composetimer.extensions.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.composetimer.ui.theme.ComposeTimerTheme

@Composable
fun ActivityBaseTheme(
    content: @Composable () -> Unit,
) = ComposeTimerTheme {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        content()
    }
}

@Composable
fun CText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    fontSize: TextUnit = 18.sp,
) = Text(
    text = text,
    modifier = modifier,
    color = color,
    fontSize = fontSize,
)

@Composable
fun CTextField(
    value: String,
    modifier: Modifier = Modifier,
    colors: TextFieldColors = TextFieldDefaults.colors()
) {
    var mutableValue = value
    TextField(
        value = mutableValue,
        onValueChange = {
            mutableValue = it
        },
        colors = colors,
        modifier = modifier
    )
}


@Composable
fun CButton(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    onClick: () -> Unit,
) = Button(
    onClick = onClick,
    modifier = modifier,
) {
    CText(text = text, color = color)
}
