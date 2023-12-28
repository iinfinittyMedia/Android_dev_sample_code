package com.example.stopwatch.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stopwatch.R

@Composable
fun CountDownButton(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onClick: () -> Unit,
    title: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp))
    {
        Button(
            onClick = {
                onClick()
            },
            modifier =
            Modifier
                .height(70.dp)
                .width(200.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.run {
                buttonColors(
                        containerColor = colorResource(id = R.color.pink),
                        contentColor = colorResource(id = R.color.white),
                    )
            },
            )
        {
            Text(
                title,
                fontSize = 20.sp,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.poppins_semibold))
            )
        }
    }
}

@Preview
@Composable
fun PreButton() {
    CountDownButton(isPlaying = true, onClick = { /*TODO*/ }, title = "Start")
}