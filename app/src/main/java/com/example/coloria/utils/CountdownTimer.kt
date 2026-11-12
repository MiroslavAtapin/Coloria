package com.example.coloria.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.coloria.ui.theme.ColoriaTheme
import kotlinx.coroutines.delay

private const val TICK_MS = 10L

@Composable
fun CountdownTimer(
    seconds: Int,
    modifier: Modifier = Modifier,
    onFinish: () -> Unit = {},
) {
    val totalMillis = seconds * 1000L
    var millisLeft by remember(seconds) { mutableLongStateOf(totalMillis) }

    LaunchedEffect(seconds) {
        millisLeft = totalMillis
        while (millisLeft > 0) {
            delay(TICK_MS)
            millisLeft = (millisLeft - TICK_MS).coerceAtLeast(0)
        }
        onFinish()
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = formatTime(millisLeft),
            style = ColoriaTheme.typography.headlineSmall,
            color = ColoriaTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

private fun formatTime(millis: Long): String {
    val wholeSeconds = millis / 1000
    val hundredths = (millis % 1000) / 10
    return "$wholeSeconds.${hundredths.toString().padStart(2, '0')}"
}
