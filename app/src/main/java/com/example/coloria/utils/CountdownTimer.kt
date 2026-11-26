package com.example.coloria.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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

private const val QUIET_FRACTION = 0.45f
private const val SLOW_PERIOD_MS = 280f
private const val FAST_PERIOD_MS = 90f
private const val MIN_PULSE_INTENSITY = 0.50f
private const val MAX_PULSE_INTENSITY = 1.00f


internal class TimerPulsePacer(private val totalMillis: Long) {

    private var nextPulseRemaining = NOT_SCHEDULED


    fun reset() {
        nextPulseRemaining = NOT_SCHEDULED
    }


    fun advance(remainingMillis: Long): Float? {
        val total = totalMillis.coerceAtLeast(1L)
        val quietMillis = total * QUIET_FRACTION

        if (remainingMillis > quietMillis) return null

        if (nextPulseRemaining == NOT_SCHEDULED) {
            nextPulseRemaining = remainingMillis
        }

        if (remainingMillis > nextPulseRemaining) return null

        val progress = (1f - remainingMillis.toFloat() / total).coerceIn(QUIET_FRACTION, 1f)
        val ramp = (progress - QUIET_FRACTION) / (1f - QUIET_FRACTION)
        val period = SLOW_PERIOD_MS + (FAST_PERIOD_MS - SLOW_PERIOD_MS) * ramp

        nextPulseRemaining = remainingMillis - period.toLong()

        return MIN_PULSE_INTENSITY + (MAX_PULSE_INTENSITY - MIN_PULSE_INTENSITY) * ramp
    }


    private companion object {
        const val NOT_SCHEDULED = -1L
    }
}


@Composable
fun CountdownTimer(
    seconds: Int,
    modifier: Modifier = Modifier,
    onFinish: () -> Unit = {},
) {
    val totalMillis = seconds * 1000L
    var millisLeft by remember(seconds) { mutableLongStateOf(totalMillis) }
    val haptics = LocalHaptics.current
    val pacer = remember(seconds) { TimerPulsePacer(totalMillis) }

    LaunchedEffect(seconds) {
        millisLeft = totalMillis
        pacer.reset()
        haptics.timerStart()

        while (millisLeft > 0) {
            delay(TICK_MS)
            millisLeft = (millisLeft - TICK_MS).coerceAtLeast(0)

            val pulse = pacer.advance(millisLeft)
            if (pulse != null) haptics.timerPulse(pulse)
        }
        onFinish()
    }

    DisposableEffect(seconds) {
        onDispose { haptics.cancel() }
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
