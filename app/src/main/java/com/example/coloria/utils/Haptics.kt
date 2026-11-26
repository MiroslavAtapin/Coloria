package com.example.coloria.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView


interface Haptics {


    fun press()


    fun selection()


    fun success()


    fun error()


    fun record()


    fun timerStart()


    fun timerPulse(intensity: Float)


    fun cancel()
}


object NoHaptics : Haptics {

    override fun press() {
    }


    override fun selection() {
    }


    override fun success() {
    }


    override fun error() {
    }


    override fun record() {
    }


    override fun timerStart() {
    }


    override fun timerPulse(intensity: Float) {
    }


    override fun cancel() {
    }
}


val LocalHaptics = compositionLocalOf<Haptics> { NoHaptics }


class AndroidHaptics(
    context: Context,
    private val view: View?,
) : Haptics {

    var enabled: Boolean = true

    private val vibrator: Vibrator? = resolveVibrator(context)

    private val hasAmplitudeControl: Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && vibrator?.hasAmplitudeControl() == true


    override fun press() {
        if (!isActive()) return

        if (performSystemHaptic(HapticFeedbackConstants.VIRTUAL_KEY)) return

        oneShot(durationMs = PressDurationMs, intensity = PressIntensity)
    }


    override fun selection() {
        if (!isActive()) return

        if (performSystemHaptic(HapticFeedbackConstants.CLOCK_TICK)) return

        oneShot(durationMs = SelectionDurationMs, intensity = SelectionIntensity)
    }


    override fun success() {
        if (!isActive()) return

        waveform(
            timings = longArrayOf(0L, SuccessDurationMs),
            amplitudes = intArrayOf(0, amplitude(SuccessIntensity)),
        )
    }


    override fun error() {
        if (!isActive()) return

        waveform(
            timings = longArrayOf(0L, ErrorPulseMs, ErrorGapMs, ErrorPulseMs),
            amplitudes = intArrayOf(0, amplitude(ErrorIntensity), 0, amplitude(ErrorIntensity)),
        )
    }


    override fun record() {
        if (!isActive()) return

        waveform(
            timings = longArrayOf(
                0L,
                RecordPulseMs,
                RecordGapMs,
                RecordPulseMs,
                RecordGapMs,
                RecordPulseMs,
            ),
            amplitudes = intArrayOf(
                0,
                amplitude(RecordFirstIntensity),
                0,
                amplitude(RecordSecondIntensity),
                0,
                amplitude(RecordThirdIntensity),
            ),
        )
    }


    override fun timerStart() {
        if (!isActive()) return

        oneShot(durationMs = TimerStartDurationMs, intensity = TimerStartIntensity)
    }


    override fun timerPulse(intensity: Float) {
        if (!isActive()) return

        val progress = intensity.coerceIn(0f, 1f)

        oneShot(
            durationMs = lerp(TimerPulseMinDurationMs, TimerPulseMaxDurationMs, progress).toLong(),
            intensity = lerp(TimerPulseMinIntensity, TimerPulseMaxIntensity, progress),
        )
    }


    override fun cancel() {
        vibrator?.cancel()
    }


    private fun isActive(): Boolean = enabled && vibrator?.hasVibrator() == true


    private fun performSystemHaptic(constant: Int): Boolean {
        val target = view ?: return false
        if (!target.isHapticFeedbackEnabled) return false

        target.performHapticFeedback(constant)
        return true
    }


    private fun oneShot(durationMs: Long, intensity: Float) {
        val target = vibrator ?: return
        val duration = durationMs.coerceAtLeast(1L)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            target.vibrate(VibrationEffect.createOneShot(duration, amplitude(intensity)))
        } else {
            @Suppress("DEPRECATION")
            target.vibrate(duration)
        }
    }


    private fun waveform(timings: LongArray, amplitudes: IntArray) {
        val target = vibrator ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            target.vibrate(VibrationEffect.createWaveform(timings, amplitudes, NoRepeat))
        } else {
            @Suppress("DEPRECATION")
            target.vibrate(timings.sum())
        }
    }


    private fun amplitude(intensity: Float): Int {
        val clamped = intensity.coerceIn(0f, 1f)

        if (!hasAmplitudeControl) return MaxAmplitude

        return (MaxAmplitude * clamped).toInt().coerceIn(1, MaxAmplitude)
    }


    private fun resolveVibrator(context: Context): Vibrator? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            manager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }


    private companion object {
        const val NoRepeat = -1
        const val MaxAmplitude = 255

        const val PressDurationMs = 10L
        const val PressIntensity = 0.40f

        const val SelectionDurationMs = 7L
        const val SelectionIntensity = 0.28f

        const val SuccessDurationMs = 18L
        const val SuccessIntensity = 0.72f

        const val ErrorPulseMs = 16L
        const val ErrorGapMs = 70L
        const val ErrorIntensity = 0.45f

        const val RecordPulseMs = 24L
        const val RecordGapMs = 74L
        const val RecordFirstIntensity = 0.50f
        const val RecordSecondIntensity = 0.72f
        const val RecordThirdIntensity = 1.00f

        const val TimerStartDurationMs = 22L
        const val TimerStartIntensity = 0.85f

        const val TimerPulseMinDurationMs = 18f
        const val TimerPulseMaxDurationMs = 42f
        const val TimerPulseMinIntensity = 0.50f
        const val TimerPulseMaxIntensity = 1.00f
    }
}


private fun lerp(start: Float, end: Float, fraction: Float): Float =
    start + (end - start) * fraction


@Composable
fun HapticsProvider(
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current.applicationContext
    val view = LocalView.current
    val haptics = remember(context, view) { AndroidHaptics(context, view) }

    SideEffect { haptics.enabled = enabled }

    CompositionLocalProvider(
        LocalHaptics provides haptics,
        content = content,
    )
}