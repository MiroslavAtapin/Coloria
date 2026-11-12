package com.example.coloria.utils

import androidx.compose.ui.graphics.Color
import kotlin.math.pow
import kotlin.math.sqrt

object ColorMatchScale {
    val maxDifference = 100.0
}

fun colorSimilarityPercent(first: Color, second: Color): Int {
    val difference = deltaE(srgbToLab(first), srgbToLab(second))
    val percent = 100.0 - (difference / ColorMatchScale.maxDifference) * 100.0
    return percent.coerceIn(0.0, 100.0).toInt()
}

private data class Lab(val l: Double, val a: Double, val b: Double)

private fun deltaE(first: Lab, second: Lab): Double {
    val dl = second.l - first.l
    val da = second.a - first.a
    val db = second.b - first.b
    return sqrt(dl * dl + da * da + db * db)
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
        b = 200.0 * (fy - fz),
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
    if (value > LAB_DELTA) value.pow(1.0 / 3.0) else value / (3.0 * LAB_DELTA * LAB_DELTA) + 4.0 / 29.0

private const val WHITE_X = 0.95047
private const val WHITE_Y = 1.00000
private const val WHITE_Z = 1.08883
private const val LAB_DELTA = 6.0 / 29.0
