package com.example.coloria.utils

import android.util.Log
import androidx.compose.ui.graphics.Color
import kotlin.math.roundToInt

const val COLORIA_LOG_TAG = "Coloria"

private fun Float.toChannel(): Int =
    (this * 255f).roundToInt().coerceIn(0, 255)

fun Color.toRgbString(): String =
    "rgb(${red.toChannel()}, ${green.toChannel()}, ${blue.toChannel()})"

fun Color.toHexString(): String {
    fun Int.hex(): String = toString(16).padStart(2, '0').uppercase()

    return "#" + red.toChannel().hex() +
            green.toChannel().hex() +
            blue.toChannel().hex()
}

fun logRoundColors(
    target: Color,
    picked: Color,
    similarityPercent: Int,
) {
    Log.d(
        COLORIA_LOG_TAG,
        "Загаданный цвет: ${target.toRgbString()} ${target.toHexString()}",
    )
    Log.d(
        COLORIA_LOG_TAG,
        "Ваш цвет: ${picked.toRgbString()} ${picked.toHexString()}",
    )
    Log.d(COLORIA_LOG_TAG, "Совпадение: $similarityPercent%")
}
