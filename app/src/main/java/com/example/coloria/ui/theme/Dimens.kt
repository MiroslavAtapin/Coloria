package com.example.coloria.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Spacing scale.
 *
 * Every screen must keep a [screenHorizontal] (16 dp) gap from the left/right screen edge to
 * the main content — implemented via padding or margin, whichever the layout needs.
 */
object ColoriaSpacing {
    val none = 0.dp

    /** Hairline / minimal gap. */
    val xxs = 2.dp

    /** Very tight gap between related elements. */
    val xs = 4.dp

    /** Tight gap (icon ↔ label). */
    val sm = 8.dp

    /** Standard inner gap. */
    val md = 12.dp

    /** Default gap between blocks. */
    val lg = 16.dp

    /** Section gap. */
    val xl = 24.dp

    /** Large section gap. */
    val xxl = 32.dp

    /** Page-level vertical padding. */
    val xxxl = 40.dp

    /**
     * The single horizontal screen inset required on every screen (16 dp).
     * Use for screen paddings, headers, cards and text blocks.
     */
    val screenHorizontal = 16.dp

    /** Recommended vertical screen inset (top/bottom content padding). */
    val screenVertical = 16.dp
}
