package com.example.coloria.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle


@Immutable
data class ColoriaTextStyles(

    val headlineLarge: TextStyle = HeadlineLargeStyle,

    val headlineSmall: TextStyle = HeadlineSmallStyle,

    val titleLarge: TextStyle = TitleLargeStyle,

    val titleMedium: TextStyle = TitleMediumStyle,

    val titleSmallSuper: TextStyle = TitleSmallSuperStyle,

    val bodyLarge: TextStyle = BodyLargeStyle,

    val bodyMedium: TextStyle = BodyMediumStyle,

    val bodySmall: TextStyle = BodySmallStyle,
)

internal val ColoriaTextTokens = ColoriaTextStyles()

internal val LocalColoriaTextStyles = staticCompositionLocalOf { ColoriaTextTokens }
