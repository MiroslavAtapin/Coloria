
package com.example.coloria.utils

import androidx.compose.ui.graphics.Color
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object ColorMatchScale {
    const val maxDifference = 100.0
}

fun colorSimilarityPercent(first: Color, second: Color): Int {
    val difference = deltaE2000(
        srgbToLab(first),
        srgbToLab(second)
    )

    val percent = 100.0 -
            (difference / ColorMatchScale.maxDifference) * 100.0

    return percent.coerceIn(0.0, 100.0).toInt()
}

private data class Lab(
    val l: Double,
    val a: Double,
    val b: Double
)

private fun deltaE2000(first: Lab, second: Lab): Double {
    val l1 = first.l
    val a1 = first.a
    val b1 = first.b

    val l2 = second.l
    val a2 = second.a
    val b2 = second.b

    val avgL = (l1 + l2) / 2.0

    val c1 = sqrt(a1 * a1 + b1 * b1)
    val c2 = sqrt(a2 * a2 + b2 * b2)
    val avgC = (c1 + c2) / 2.0

    val g = 0.5 * (
            1.0 - sqrt(
                avgC.pow(7) / (avgC.pow(7) + 25.0.pow(7))
            )
            )

    val a1Prime = (1.0 + g) * a1
    val a2Prime = (1.0 + g) * a2

    val c1Prime = sqrt(a1Prime * a1Prime + b1 * b1)
    val c2Prime = sqrt(a2Prime * a2Prime + b2 * b2)

    fun hueAngle(a: Double, b: Double): Double {
        if (a == 0.0 && b == 0.0) return 0.0
        return (Math.toDegrees(atan2(b, a)) + 360.0) % 360.0
    }

    val h1Prime = hueAngle(a1Prime, b1)
    val h2Prime = hueAngle(a2Prime, b2)

    val deltaLPrime = l2 - l1
    val deltaCPrime = c2Prime - c1Prime

    val deltaHue = when {
        c1Prime * c2Prime == 0.0 -> 0.0

        abs(h2Prime - h1Prime) <= 180.0 ->
            h2Prime - h1Prime

        h2Prime <= h1Prime ->
            h2Prime - h1Prime + 360.0

        else ->
            h2Prime - h1Prime - 360.0
    }

    val deltaHPrime = 2.0 * sqrt(c1Prime * c2Prime) *
            sin(Math.toRadians(deltaHue / 2.0))

    val avgLPrime = (l1 + l2) / 2.0
    val avgCPrime = (c1Prime + c2Prime) / 2.0

    val avgHuePrime = when {
        c1Prime * c2Prime == 0.0 ->
            h1Prime + h2Prime

        abs(h1Prime - h2Prime) <= 180.0 ->
            (h1Prime + h2Prime) / 2.0

        h1Prime + h2Prime < 360.0 ->
            (h1Prime + h2Prime + 360.0) / 2.0

        else ->
            (h1Prime + h2Prime - 360.0) / 2.0
    }

    val t = 1.0 -
            0.17 * cos(Math.toRadians(avgHuePrime - 30.0)) +
            0.24 * cos(Math.toRadians(2.0 * avgHuePrime)) +
            0.32 * cos(Math.toRadians(3.0 * avgHuePrime + 6.0)) -
            0.20 * cos(Math.toRadians(4.0 * avgHuePrime - 63.0))

    val deltaTheta = 30.0 * exp(
        -(((avgHuePrime - 275.0) / 25.0).pow(2))
    )

    val avgCPrime7 = avgCPrime.pow(7)
    val rc = 2.0 * sqrt(
        avgCPrime7 / (avgCPrime7 + 25.0.pow(7))
    )

    val sl = 1.0 +
            (0.015 * (avgLPrime - 50.0).pow(2)) /
            sqrt(20.0 + (avgLPrime - 50.0).pow(2))

    val sc = 1.0 + 0.045 * avgCPrime
    val sh = 1.0 + 0.015 * avgCPrime * t

    val rt = -sin(Math.toRadians(2.0 * deltaTheta)) * rc

    val lTerm = deltaLPrime / sl
    val cTerm = deltaCPrime / sc
    val hTerm = deltaHPrime / sh

    return sqrt(
        lTerm * lTerm +
                cTerm * cTerm +
                hTerm * hTerm +
                rt * cTerm * hTerm
    )
}

private fun srgbToLab(color: Color): Lab {
    val r = linearize(color.red)
    val g = linearize(color.green)
    val b = linearize(color.blue)

    val x = r * 0.4124564 + g * 0.3575761 + b * 0.1804375
    val y = r * 0.2126729 + g * 0.7151522 + b * 0.0721750
    val z = r * 0.0193339 + g * 0.1191920 + b * 0.9503041

    val xr = x / WHITE_X
    val yr = y / WHITE_Y
    val zr = z / WHITE_Z

    val fx = labF(xr)
    val fy = labF(yr)
    val fz = labF(zr)

    return Lab(
        l = 116.0 * fy - 16.0,
        a = 500.0 * (fx - fy),
        b = 200.0 * (fy - fz)
    )
}

private fun linearize(channel: Float): Double {
    val value = channel.toDouble()

    return if (value <= 0.04045) {
        value / 12.92
    } else {
        ((value + 0.055) / 1.055).pow(2.4)
    }
}

private fun labF(value: Double): Double =
    if (value > LAB_DELTA_CUBED) {
        value.pow(1.0 / 3.0)
    } else {
        value / (3.0 * LAB_DELTA * LAB_DELTA) + 4.0 / 29.0
    }

private const val WHITE_X = 0.95047
private const val WHITE_Y = 1.00000
private const val WHITE_Z = 1.08883
private const val LAB_DELTA = 6.0 / 29.0
private const val LAB_DELTA_CUBED = LAB_DELTA * LAB_DELTA * LAB_DELTA
