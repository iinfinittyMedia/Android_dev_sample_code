package com.example.myapplication.app.component

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import java.lang.reflect.Modifier

@Composable
fun CustomIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    backgroundColor: androidx.compose.ui.graphics.Color
) {
    IconButton(
        onClick = onClick,
        modifier = androidx.compose.ui.Modifier
            .size(50.dp)
            .background(
                color = backgroundColor,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = androidx.compose.ui.graphics.Color.White
        )
    }
}
