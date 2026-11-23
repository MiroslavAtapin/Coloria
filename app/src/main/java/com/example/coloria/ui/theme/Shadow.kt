package com.example.coloria.ui.theme

import android.graphics.BlurMaskFilter
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


data class ColoriaShadowLayer(
    val x: Dp = 0.dp,
    val y: Dp = 0.dp,
    val blur: Dp = 0.dp,
    val spread: Dp = 0.dp,
    val color: Color = ColoriaPalette.ShadowColor,
)


data class ColoriaShadow(
    val layers: List<ColoriaShadowLayer>,
) {
    val isVisible: Boolean get() = layers.isNotEmpty()
}


object ColoriaShadows {

    private fun layer(y: Dp, blur: Dp, spread: Dp) = ColoriaShadowLayer(
        x = 0.dp,
        y = y,
        blur = blur,
        spread = spread,
        color = ColoriaPalette.ShadowColor,
    )


    val Small = ColoriaShadow(
        layers = listOf(
            layer(y = 6.dp, blur = 12.dp, spread = (-6).dp),
            layer(y = 8.dp, blur = 24.dp, spread = (-4).dp),
        ),
    )


    val Medium = ColoriaShadow(
        layers = listOf(
            layer(y = 8.dp, blur = 18.dp, spread = (-6).dp),
            layer(y = 12.dp, blur = 42.dp, spread = (-4).dp),
        ),
    )


    val None = ColoriaShadow(layers = emptyList())
}


internal object ColoriaShadowRenderer {

    private const val MIN_BLUR_DP = 0.01f


    fun DrawScope.drawShadow(
        size: Size,
        cornerRadiusPx: Float,
        shadow: ColoriaShadow,
        shapeColor: Color,
    ) {
        if (!shadow.isVisible || size.minDimension <= 0f) return

        val radius = cornerRadiusPx.coerceIn(0f, size.minDimension / 2f)
        val androidPath = Path().apply {
            addRoundRect(
                RoundRect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height,
                    radiusX = radius,
                    radiusY = radius,
                )
            )
        }.asAndroidPath()

        shadow.layers.sortedByDescending { it.blur.value }.forEach { layer ->
            val blurPx = layer.blur.toPx()
            val spreadPx = layer.spread.toPx()
            val minBlurPx = MIN_BLUR_DP.dp.toPx()
            val native = Paint().asFrameworkPaint().apply { isAntiAlias = true }

            if (spreadPx < 0f) {
                native.color = layer.color.toArgb()
                native.maskFilter = BlurMaskFilter(
                    ((blurPx + spreadPx) / 2f).coerceAtLeast(minBlurPx),
                    BlurMaskFilter.Blur.NORMAL,
                )
            } else {
                native.color = android.graphics.Color.TRANSPARENT
                native.setShadowLayer(
                    blurPx.coerceAtLeast(minBlurPx),
                    layer.x.toPx(),
                    layer.y.toPx(),
                    layer.color.toArgb(),
                )
                if (spreadPx > 0f) {
                    native.maskFilter = BlurMaskFilter(spreadPx / 2f, BlurMaskFilter.Blur.NORMAL)
                }
            }
            drawIntoCanvas { canvas -> canvas.nativeCanvas.drawPath(androidPath, native) }

            native.clearShadowLayer()
            native.maskFilter = null
        }
    }
}
