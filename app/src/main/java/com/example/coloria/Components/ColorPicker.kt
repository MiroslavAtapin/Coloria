package com.example.coloria.Components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.coloria.ui.theme.ColoriaRadii
import com.example.coloria.ui.theme.ColoriaSpacing

private val HueColors = listOf(
    Color(0xFFFF0000),
    Color(0xFFFFFF00),
    Color(0xFF00FF00),
    Color(0xFF00FFFF),
    Color(0xFF0000FF),
    Color(0xFFFF00FF),
    Color(0xFFFF0000),
)

private val SliderWidth = 24.dp
private const val PickerAspectRatio = 250f / 180f

@Composable
fun ColorPicker(
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    var fieldSize by remember { mutableStateOf(IntSize.Zero) }
    var fieldPosition by remember { mutableStateOf(Offset.Zero) }
    var sliderSize by remember { mutableStateOf(IntSize.Zero) }
    var sliderPosition by remember { mutableStateOf(0f) }
    var hueColor by remember { mutableStateOf(Color.Red) }

    val cornerRadius = RoundedCornerShape(ColoriaRadii.Small)
    val markerColor = Color.White
    val markerShadow = Color.Black.copy(alpha = 0.4f)

    fun updateColor(position: Offset) {
        if (fieldSize.width == 0 || fieldSize.height == 0) return
        onColorChange(colorAt(position, fieldSize, hueColor))
    }

    fun updateHue(positionY: Float) {
        sliderPosition = positionY
        hueColor = hueAt(positionY, sliderSize)
        updateColor(fieldPosition)
    }

    BoxWithConstraints(modifier = modifier) {
        val sliderWidth = SliderWidth
        val gapWidth = ColoriaSpacing.md
        val fieldWidth = maxWidth - sliderWidth - gapWidth
        val fieldHeight = fieldWidth / PickerAspectRatio

        Row {
        Box(
            modifier = Modifier
                .height(fieldHeight)
                .width(fieldWidth)
                .clip(cornerRadius)
                .onSizeChanged { fieldSize = it }
                .pointerInput(hueColor) {
                    detectTapGestures { offset ->
                        fieldPosition = clampToField(offset, fieldSize)
                        updateColor(fieldPosition)
                    }
                }
                .pointerInput(hueColor) {
                    detectDragGestures { change, _ ->
                        fieldPosition = clampToField(change.position, fieldSize)
                        updateColor(fieldPosition)
                    }
                },
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(Brush.horizontalGradient(listOf(Color.White, hueColor)))
                drawRect(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                val indicatorRadius = 8.dp.toPx()
                val outline = 2.dp.toPx()
                val edge = indicatorRadius + outline
                val safeCenter = Offset(
                    x = fieldPosition.x.coerceIn(edge, (size.width - edge).coerceAtLeast(edge)),
                    y = fieldPosition.y.coerceIn(edge, (size.height - edge).coerceAtLeast(edge)),
                )

                drawCircle(color = markerShadow, radius = indicatorRadius, center = safeCenter)
                drawCircle(
                    color = markerColor,
                    radius = indicatorRadius,
                    center = safeCenter,
                    style = Stroke(width = outline),
                )
            }
        }

        Spacer(modifier = Modifier.width(ColoriaSpacing.md))

        Box(
            modifier = Modifier
                .height(fieldHeight)
                .width(SliderWidth)
                .clip(RoundedCornerShape(12.dp))
                .onSizeChanged { sliderSize = it }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        updateHue(offset.y.coerceIn(0f, sliderSize.height.toFloat()))
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        updateHue(change.position.y.coerceIn(0f, sliderSize.height.toFloat()))
                    }
                },
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(Brush.verticalGradient(HueColors))
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                val indicatorRadius = 8.dp.toPx()
                val outline = 2.dp.toPx()
                val edge = indicatorRadius + outline
                val safeY = sliderPosition.coerceIn(edge, (size.height - edge).coerceAtLeast(edge))

                drawCircle(
                    color = markerShadow,
                    radius = indicatorRadius,
                    center = Offset(size.width / 2f, safeY),
                )
                drawCircle(
                    color = markerColor,
                    radius = indicatorRadius,
                    center = Offset(size.width / 2f, safeY),
                    style = Stroke(width = outline),
                )
            }
        }
        }
    }
}

private fun clampToField(offset: Offset, size: IntSize): Offset =
    Offset(
        x = offset.x.coerceIn(0f, size.width.toFloat()),
        y = offset.y.coerceIn(0f, size.height.toFloat()),
    )

private fun colorAt(position: Offset, size: IntSize, hueColor: Color): Color {
    if (size.width == 0 || size.height == 0) return hueColor

    val whiteFactor = 1f - (position.x / size.width)
    val blackFactor = position.y / size.height

    return Color(
        red = applyLightDark(hueColor.red, whiteFactor, blackFactor),
        green = applyLightDark(hueColor.green, whiteFactor, blackFactor),
        blue = applyLightDark(hueColor.blue, whiteFactor, blackFactor),
    )
}

private fun applyLightDark(channel: Float, whiteFactor: Float, blackFactor: Float): Float {
    val lightened = channel + (1f - channel) * whiteFactor
    return lightened * (1f - blackFactor)
}

private fun hueAt(positionY: Float, size: IntSize): Color {
    if (size.height == 0) return Color.Red

    val progress = (positionY / size.height).coerceIn(0f, 1f)
    val segments = HueColors.size - 1
    val scaled = progress * segments
    val index = scaled.toInt().coerceIn(0, segments - 1)
    val local = scaled - index

    val start = HueColors[index]
    val end = HueColors[index + 1]

    return Color(
        red = lerp(start.red, end.red, local),
        green = lerp(start.green, end.green, local),
        blue = lerp(start.blue, end.blue, local),
    )
}

private fun lerp(start: Float, end: Float, fraction: Float): Float =
    start + (end - start) * fraction
