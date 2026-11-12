package com.example.coloria.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

/**
 * The approved text scale as a token set, read through [ColoriaTheme.typography].
 *
 * Every style below uses the Mulish family (see Type.kt). No other sizes/weights exist in
 * the app — if a new style is ever needed, add it here first.
 */
@Immutable
data class ColoriaTextStyles(
    /** 32 / 40, Bold */
    val headlineLarge: TextStyle = HeadlineLargeStyle,
    /** 24 / 32, Medium */
    val headlineSmall: TextStyle = HeadlineSmallStyle,
    /** 20 / 28, Bold */
    val titleLarge: TextStyle = TitleLargeStyle,
    /** 16 / 24, Medium */
    val titleMedium: TextStyle = TitleMediumStyle,
    /** 14 / 20, Bold ("title small-super") */
    val titleSmallSuper: TextStyle = TitleSmallSuperStyle,
    /** 16 / 24, Regular */
    val bodyLarge: TextStyle = BodyLargeStyle,
    /** 14 / 20, Regular */
    val bodyMedium: TextStyle = BodyMediumStyle,
    /** 12 / 16, Regular */
    val bodySmall: TextStyle = BodySmallStyle,
)

internal val ColoriaTextTokens = ColoriaTextStyles()

internal val LocalColoriaTextStyles = staticCompositionLocalOf { ColoriaTextTokens }
