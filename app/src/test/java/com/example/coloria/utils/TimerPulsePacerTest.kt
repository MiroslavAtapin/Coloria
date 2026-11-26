package com.example.coloria.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

private const val TOTAL_MS = 2000L
private const val TICK_MS = 10L

class TimerPulsePacerTest {


    @Test
    fun noPulsesWhileThereIsPlentyOfTimeLeft() {
        val pacer = TimerPulsePacer(TOTAL_MS)
        pacer.reset()

        val pulses = mutableListOf<Float>()
        var remaining = TOTAL_MS
        while (remaining - TICK_MS > TOTAL_MS * QUIET_THRESHOLD) {
            remaining -= TICK_MS
            pacer.advance(remaining)?.let { pulses += it }
        }

        assertTrue("Expected silence before the quiet threshold, got $pulses", pulses.isEmpty())
    }


    @Test
    fun pulsesAppearInTheFinalStretchOfTheTimer() {
        val pacer = TimerPulsePacer(TOTAL_MS)
        pacer.reset()

        val pulses = mutableListOf<Float>()
        var remaining = TOTAL_MS
        while (remaining > 0) {
            remaining -= TICK_MS
            pacer.advance(remaining)?.let { pulses += it }
        }

        assertTrue("Expected pulses near the end of the timer", pulses.isNotEmpty())
    }


    @Test
    fun intensityAndDensityRampUpWithoutExceedingComfortBounds() {
        val pacer = TimerPulsePacer(TOTAL_MS)
        pacer.reset()

        val pulseTimes = mutableListOf<Long>()
        val pulseIntensities = mutableListOf<Float>()
        var remaining = TOTAL_MS
        var elapsed = 0L
        while (remaining > 0) {
            remaining -= TICK_MS
            elapsed += TICK_MS
            pacer.advance(remaining)?.let {
                pulseTimes += elapsed
                pulseIntensities += it
            }
        }

        assertTrue("Pulse intensities must not decrease", pulseIntensities == pulseIntensities.sorted())
        assertTrue("Intensity must stay within 0..1", pulseIntensities.all { it in 0f..1f })
        assertTrue("Intensity must not exceed full amplitude", pulseIntensities.max() <= 1.0f)
        assertTrue("Last pulses must feel pronounced", pulseIntensities.last() >= 0.95f)

        val gaps = pulseTimes.zipWithNext { first, second -> second - first }
        assertTrue(
            "Pulses must get closer together over time, got $gaps",
            gaps == gaps.sortedDescending(),
        )
        assertTrue("Pulses must never be faster than the cap", gaps.min() >= MIN_EXPECTED_GAP_MS)
    }


    @Test
    fun noPulseWhileRemainingTimeIsAboveThreshold() {
        val pacer = TimerPulsePacer(TOTAL_MS)
        pacer.reset()

        assertNull(pacer.advance(TOTAL_MS - TICK_MS))
        assertNull(pacer.advance((TOTAL_MS * 0.45f + 1f).toLong()))
    }


    @Test
    fun resetRestartsThePacingCycle() {
        val pacer = TimerPulsePacer(TOTAL_MS)

        var remaining = TOTAL_MS
        while (remaining > 0) {
            remaining -= TICK_MS
            pacer.advance(remaining)
        }

        pacer.reset()

        val pulsesAfterReset = mutableListOf<Float>()
        remaining = TOTAL_MS
        while (remaining > 0) {
            remaining -= TICK_MS
            pacer.advance(remaining)?.let { pulsesAfterReset += it }
        }

        assertTrue("Expected the pacing cycle to start over after reset", pulsesAfterReset.isNotEmpty())
    }


    private companion object {
        const val MIN_EXPECTED_GAP_MS = 80L
        const val QUIET_THRESHOLD = 0.45f
    }
}