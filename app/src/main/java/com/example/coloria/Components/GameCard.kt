package com.example.coloria.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.coloria.ui.theme.ColoriaRadii
import com.example.coloria.ui.theme.ColoriaShadowRenderer
import com.example.coloria.ui.theme.ColoriaShadows
import com.example.coloria.ui.theme.ColoriaSpacing
import com.example.coloria.ui.theme.ColoriaTheme
import kotlin.math.pow

private const val CARD_ASPECT_RATIO = 370f / 430f

@Composable
fun GameCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = rememberRandomCardColor(),
    splitColor: Color? = null,
    splitLabels: Pair<String, String>? = null,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    val cornerRadius = ColoriaRadii.Medium
    val shape = RoundedCornerShape(cornerRadius)

    Box(
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
            },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ColoriaSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(ColoriaSpacing.lg),
            content = content,
        )

        if (splitLabels != null && splitColor != null) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(ColoriaSpacing.md),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                SplitLabel(
                    text = splitLabels.first,
                    backgroundColor = backgroundColor,
                    modifier = Modifier.weight(1f),
                )
                SplitLabel(
                    text = splitLabels.second,
                    backgroundColor = splitColor,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun SplitLabel(
    text: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = ColoriaTheme.typography.bodyMedium,
        color = adaptiveTextColor(backgroundColor, ColoriaTheme.colors.textPrimary),
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}

private fun adaptiveTextColor(background: Color, darkColor: Color): Color =
    if (relativeLuminance(background) > 0.5f) darkColor else Color.White

private fun relativeLuminance(color: Color): Float =
    0.2126f * toLinear(color.red) +
        0.7152f * toLinear(color.green) +
        0.0722f * toLinear(color.blue)

private fun toLinear(channel: Float): Float =
    if (channel <= 0.04045f) {
        channel / 12.92f
    } else {
        ((channel + 0.055f) / 1.055f).pow(2.4f)
    }

@Composable
private fun rememberRandomCardColor(): Color = remember {
    Color(
        red = (0..255).random(),
        green = (0..255).random(),
        blue = (0..255).random(),
    )
}
