package com.example.coloria.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


@Immutable
object ColoriaPalette {

    val BackgroundPrimary = Color(0xFFFAF9F5)


    val BackgroundSurface = Color(0xFFF0EEE8)

    val AccentPrimary = Color(0xFFE76F51)


    val AccentSecondary = Color(0xFFF6C6BB)

    val TextPrimary = Color(0xFF242522)


    val TextSecondary = Color(0xFF77766F)

    val Border = Color(0xFFBCBCBC)

    val StateGood = Color(0xFF278A55)


    val StateBad = Color(0xFFD94F4F)


    val StateGoodSoft = Color(0xFFD9EAE1)

    val OnAccent = Color(0xFFFFFFFF)

    val ShadowTint = Color(0xFF18274B)


    const val SHADOW_ALPHA = 0.12f


    const val SHADOW_ALPHA_8BIT = 31


    val ShadowColor = Color(0x1F18274B)
}


@Immutable
data class ColoriaColors(
    val backgroundPrimary: Color = ColoriaPalette.BackgroundPrimary,
    val backgroundSurface: Color = ColoriaPalette.BackgroundSurface,
    val accentPrimary: Color = ColoriaPalette.AccentPrimary,
    val accentSecondary: Color = ColoriaPalette.AccentSecondary,
    val textPrimary: Color = ColoriaPalette.TextPrimary,
    val textSecondary: Color = ColoriaPalette.TextSecondary,
    val border: Color = ColoriaPalette.Border,
    val stateGood: Color = ColoriaPalette.StateGood,
    val stateGoodSoft: Color = ColoriaPalette.StateGoodSoft,
    val stateBad: Color = ColoriaPalette.StateBad,
    val onAccent: Color = ColoriaPalette.OnAccent,
    val shadowTint: Color = ColoriaPalette.ShadowTint,
)


internal val ColoriaColorTokens = ColoriaColors()


internal val LocalColoriaColors = staticCompositionLocalOf { ColoriaColorTokens }
