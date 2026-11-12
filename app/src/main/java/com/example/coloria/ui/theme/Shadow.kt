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

// ===========================================================================
// Shadow tokens
// ===========================================================================

/**
 * A single Figma drop-shadow layer.
 *
 * Mirrors the Figma fields 1:1: [x]/[y] offsets, [blur] (Figma "blur"), [spread] (Figma
 * "spread", may be negative) and [color] (alpha/opacity already baked in).
 */
data class ColoriaShadowLayer(
    val x: Dp = 0.dp,
    val y: Dp = 0.dp,
    val blur: Dp = 0.dp,
    val spread: Dp = 0.dp,
    val color: Color = ColoriaPalette.ShadowColor,
)

/** A complete Figma drop shadow, made of one or more stacked [ColoriaShadowLayer]s. */
data class ColoriaShadow(
    val layers: List<ColoriaShadowLayer>,
) {
    val isVisible: Boolean get() = layers.isNotEmpty()
}

/** Shadow tokens — the only shadows allowed in the app. */
object ColoriaShadows {

    private fun layer(y: Dp, blur: Dp, spread: Dp) = ColoriaShadowLayer(
        x = 0.dp,
        y = y,
        blur = blur,
        spread = spread,
        color = ColoriaPalette.ShadowColor,
    )

    /**
     * Figma "small":
     * `x0 y6 blur12 spread-6 #18274B 12%` + `x0 y8 blur24 spread-4 #18274B 12%`.
     */
    val Small = ColoriaShadow(
        layers = listOf(
            layer(y = 6.dp, blur = 12.dp, spread = (-6).dp),
            layer(y = 8.dp, blur = 24.dp, spread = (-4).dp),
        ),
    )

    /**
     * Figma "medium":
     * `x0 y8 blur18 spread-6 #18274B 12%` + `x0 y12 blur42 spread-4 #18274B 12%`.
     */
    val Medium = ColoriaShadow(
        layers = listOf(
            layer(y = 8.dp, blur = 18.dp, spread = (-6).dp),
            layer(y = 12.dp, blur = 42.dp, spread = (-4).dp),
        ),
    )

    /** No shadow. */
    val None = ColoriaShadow(layers = emptyList())
}

// ===========================================================================
// Shadow rendering
// ===========================================================================

/**
 * Renders the Coloria shadow system on a canvas.
 *
 * ### Figma → Android mapping
 *
 * | Figma parameter | Android implementation |
 * |-----------------|------------------------|
 * | `x`, `y`        | `Paint.setShadowLayer` dx/dy — exact |
 * | `blur`          | `Paint.setShadowLayer` radius — exact |
 * | `color` @ `12%` | ARGB handed to `setShadowLayer` — exact |
 * | `spread`        | no native equivalent → emulated with [BlurMaskFilter] |
 *
 * `spread` is the only parameter Android lacks. The design uses **negative** spread only (the
 * shadow is slightly *smaller* than the element: it hides beneath it, leaving a soft ring).
 * A negative spread is geometrically the same as a smaller blur sigma, which [BlurMaskFilter]
 * reproduces exactly, so the rendered result matches the Figma preview.
 *
 * Layers are drawn largest-blur-first, so the tighter layer sits on top — the same stacking
 * order Figma uses.
 */
internal object ColoriaShadowRenderer {

    private const val MIN_BLUR_DP = 0.01f

    /**
     * @param size            element size in px (local canvas coordinates start at 0,0)
     * @param cornerRadiusPx  element corner radius in px
     * @param shadow          the token to render
     * @param shapeColor      element fill, replays the shadow rings so the element itself
     *                        is painted by the caller afterwards
     */
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
                // Negative spread → shrink the soft edge by |spread| px.
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

            // The shadow is cast by the shape's own fill; the caller draws the real element
            // right after, so the caster is fully covered.
            drawIntoCanvas { canvas -> canvas.nativeCanvas.drawPath(androidPath, native) }

            native.clearShadowLayer()
            native.maskFilter = null
        }
    }
}
