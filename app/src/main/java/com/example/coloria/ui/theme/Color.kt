package com.example.coloria.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Raw color palette — the single source of truth for every color used in Coloria.
 *
 * Rules:
 *  - Never use a hard-coded [Color] literal in screens/components; reference the palette
 *    (or better, a semantic token from [ColoriaColors] returned by `ColoriaTheme.colors`).
 *  - The app is light-theme only, so this palette is theme-agnostic and never swapped at runtime.
 */
@Immutable
object ColoriaPalette {

    // --- Backgrounds -------------------------------------------------------
    /** App background (behind everything). */
    val BackgroundPrimary = Color(0xFFFAF9F5)

    /** Surfaces that sit on top of the background: cards, sheets, inputs. */
    val BackgroundSurface = Color(0xFFF0EEE8)

    // --- Accents -----------------------------------------------------------
    /** Primary accent: CTAs, active states, key highlights. */
    val AccentPrimary = Color(0xFFE76F51)

    /** Secondary accent: soft fills, tags, selection highlights. */
    val AccentSecondary = Color(0xFFF6C6BB)

    // --- Text --------------------------------------------------------------
    /** Primary text: headings, main content. */
    val TextPrimary = Color(0xFF242522)

    /** Secondary text: hints, captions, supporting copy. */
    val TextSecondary = Color(0xFF77766F)

    // --- Lines -------------------------------------------------------------
    /** Borders, dividers, outlines. */
    val Border = Color(0xFFBCBCBC)

    // --- States ------------------------------------------------------------
    /** Positive / success state (correct answer, win, progress). */
    val StateGood = Color(0xFF278A55)

    /** Negative / error state (wrong answer, loss, destructive). */
    val StateBad = Color(0xFFD94F4F)

    /** Soft success fill: background of the new-record capsule. */
    val StateGoodSoft = Color(0xFFD9EAE1)

    // --- On-color helpers --------------------------------------------------
    /** Content placed on a strong accent or state color (e.g. text on [AccentPrimary]). */
    val OnAccent = Color(0xFFFFFFFF)

    // --- Shadow ------------------------------------------------------------
    /**
     * Shadow tint straight from Figma (`18274B`, i.e. #18274B).
     * Registered here so the whole shadow system (see Shadow.kt) stays in one place.
     */
    val ShadowTint = Color(0xFF18274B)

    /**
     * Opacity of every shadow layer: 12% as specified in Figma.
     *
     * Android paints in 8-bit ARGB, so 12% quantises to alpha `31` out of 255
     * (`31 / 255 = 0.12156863`) — the closest representable value. [SHADOW_ALPHA] is kept for
     * readability and documentation; painting uses [ShadowColor].
     */
    const val SHADOW_ALPHA = 0.12f

    /** 8-bit alpha actually used when painting shadows (12% rounded to 31/255). */
    const val SHADOW_ALPHA_8BIT = 31

    /** Fully resolved shadow color: [ShadowTint] at exactly [SHADOW_ALPHA_8BIT] / 255 opacity. */
    val ShadowColor = Color(0x1F18274B)
}

/**
 * Semantic color tokens consumed by the UI layer.
 *
 * Every value maps to a [ColoriaPalette] entry — change a palette color and the whole app
 * (including every mini-game) follows automatically. Colors that the design does not define
 * yet are kept as named slots here so they can be added later without touching call sites.
 */
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

/** The single, immutable color set of the app (no dark variant exists by design). */
internal val ColoriaColorTokens = ColoriaColors()

/**
 * Provides the color tokens down the composition tree; installed by `ColoriaTheme`.
 */
internal val LocalColoriaColors = staticCompositionLocalOf { ColoriaColorTokens }
