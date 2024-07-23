package com.mohsenoid.wolt.ui.util

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SeparatorLine(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = Color.LightGray,
) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawLine(
            start = Offset(0f, canvasHeight / 2f),
            end = Offset(canvasWidth, canvasHeight / 2f),
            color = color,
            strokeWidth = thickness.toPx(),
        )
    }
}
