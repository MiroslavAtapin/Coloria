package com.example.coloria.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp


object ColoriaRadii {
    val Small = 16.dp
    val Medium = 40.dp


    val Maximal = 222.dp
}

object ColoriaShapes {

    val Small = RoundedCornerShape(ColoriaRadii.Small)


    val Medium = RoundedCornerShape(ColoriaRadii.Medium)


    val Circle = CircleShape


    val Pill = RoundedCornerShape(percent = 50)
}


val ColoriaMaterialShapes = Shapes(
    extraSmall = ColoriaShapes.Small,
    small = ColoriaShapes.Small,
    medium = ColoriaShapes.Small,
    large = ColoriaShapes.Medium,
    extraLarge = ColoriaShapes.Medium,
)
