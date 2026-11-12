package com.example.coloria.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.coloria.ui.theme.ColoriaRadii
import com.example.coloria.ui.theme.ColoriaShadowRenderer
import com.example.coloria.ui.theme.ColoriaShadows
import com.example.coloria.ui.theme.ColoriaSpacing

private const val CARD_ASPECT_RATIO = 370f / 440f

@Composable
fun GameCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = rememberRandomCardColor(),
    splitColor: Color? = null,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    val cornerRadius = ColoriaRadii.Medium
    val shape = RoundedCornerShape(cornerRadius)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(CARD_ASPECT_RATIO)
            .drawBehind {
                ColoriaShadowRenderer.run {
                    drawShadow(
                        size = size,
                        cornerRadiusPx = cornerRadius.toPx(),
                        shadow = ColoriaShadows.Medium,
                        shapeColor = backgroundColor,
                    )
                }
            }
            .background(backgroundColor, shape)
            .clip(shape)
            .drawBehind {
                if (splitColor != null) {
                    drawRect(
                        color = splitColor,
                        topLeft = Offset(size.width / 2f, 0f),
                        size = Size(size.width / 2f, size.height),
                    )
                }
            }
            .padding(ColoriaSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(ColoriaSpacing.lg),
        content = content,
    )
}

@Composable
private fun rememberRandomCardColor(): Color = remember {
    Color(
        red = (0..255).random(),
        green = (0..255).random(),
        blue = (0..255).random(),
    )
}
