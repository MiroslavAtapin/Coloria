package com.example.coloria.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Border-radius scale — the only radii allowed in the app.
 *
 *  - [Small]  = 16 dp  (inputs, chips, small cards)
 *  - [Medium] = 40 dp  (cards, sheets, panels)
 *  - [Circle] = the "maximal" radius, rendered as a true circle (222 dp token)
 */
object ColoriaRadii {
    val Small = 16.dp
    val Medium = 40.dp

    /** Token value from the design (222). Any radius at/above this is rendered as a circle. */
    val Maximal = 222.dp
}

object ColoriaShapes {
    /** 16 dp rounded corners. */
    val Small = RoundedCornerShape(ColoriaRadii.Small)

    /** 40 dp rounded corners. */
    val Medium = RoundedCornerShape(ColoriaRadii.Medium)

    /** Perfect circle / pill — use for avatar-like and fully rounded surfaces. */
    val Circle = CircleShape

    /** Pill variant for buttons; visually equals a circle on a 40 dp+ tall element. */
    val Pill = RoundedCornerShape(percent = 50)
}

/**
 * Material [Shapes] mapping so Material components pick up the same radii automatically.
 */
val ColoriaMaterialShapes = Shapes(
    extraSmall = ColoriaShapes.Small,
    small = ColoriaShapes.Small,
    medium = ColoriaShapes.Small,
    large = ColoriaShapes.Medium,
    extraLarge = ColoriaShapes.Medium,
)
