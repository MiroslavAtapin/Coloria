package com.example.coloria.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

/**
 * Material color scheme.
 *
 * Material3 is used purely as a component/plumbing layer: it is force-fed the Coloria palette
 * so the app's own look is never Material-driven. There is intentionally **no** `darkColorScheme`
 * in this project — the app has a single light theme and ignores the system dark setting.
 */
private val ColoriaLightColorScheme = lightColorScheme(
    primary = ColoriaPalette.AccentPrimary,
    onPrimary = ColoriaPalette.OnAccent,
    primaryContainer = ColoriaPalette.AccentSecondary,
    onPrimaryContainer = ColoriaPalette.TextPrimary,

    secondary = ColoriaPalette.AccentSecondary,
    onSecondary = ColoriaPalette.TextPrimary,
    secondaryContainer = ColoriaPalette.AccentSecondary,
    onSecondaryContainer = ColoriaPalette.TextPrimary,

    tertiary = ColoriaPalette.AccentPrimary,
    onTertiary = ColoriaPalette.OnAccent,

    background = ColoriaPalette.BackgroundPrimary,
    onBackground = ColoriaPalette.TextPrimary,

    surface = ColoriaPalette.BackgroundPrimary,
    onSurface = ColoriaPalette.TextPrimary,
    surfaceVariant = ColoriaPalette.BackgroundSurface,
    onSurfaceVariant = ColoriaPalette.TextSecondary,

    surfaceContainerLowest = ColoriaPalette.BackgroundPrimary,
    surfaceContainerLow = ColoriaPalette.BackgroundPrimary,
    surfaceContainer = ColoriaPalette.BackgroundSurface,
    surfaceContainerHigh = ColoriaPalette.BackgroundSurface,
    surfaceContainerHighest = ColoriaPalette.BackgroundSurface,

    outline = ColoriaPalette.Border,
    outlineVariant = ColoriaPalette.Border,

    error = ColoriaPalette.StateBad,
    onError = ColoriaPalette.OnAccent,

    // Deliberately neutral: the app is flat, overlays are not tinted by Material.
    scrim = Color.Black,
    inverseSurface = ColoriaPalette.TextPrimary,
    inverseOnSurface = ColoriaPalette.BackgroundPrimary,
    inversePrimary = ColoriaPalette.AccentSecondary,
)

/**
 * The one and only app theme.
 *
 * Deliberately has **no** `darkTheme` / `dynamicColor` parameters: the theme never reacts to
 * the system dark mode, to Material You / dynamic colors, or to the device's wallpaper colors.
 *
 * Provides:
 *  - [ColoriaTheme.colors] — semantic color tokens for app code
 *  - `MaterialTheme.colorScheme/typography/shapes` — the same tokens, for Material components
 */
@Composable
fun ColoriaTheme(
    content: @Composable () -> Unit,
) {
    val colors = remember { ColoriaColorTokens }
    val textStyles = remember { ColoriaTextTokens }

    CompositionLocalProvider(
        LocalColoriaColors provides colors,
        LocalColoriaTextStyles provides textStyles,
    ) {
        MaterialTheme(
            colorScheme = ColoriaLightColorScheme,
            typography = ColoriaTypography,
            shapes = ColoriaMaterialShapes,
            content = content,
        )
    }
}

/**
 * Token accessor, Compose-style:
 * ```
 * Text(
 *     text = "Hi",
 *     color = ColoriaTheme.colors.textPrimary,
 *     style = ColoriaTheme.typography.titleLarge,
 * )
 * ```
 * Like `MaterialTheme`, the design system exposes a single name: the [ColoriaTheme] object below
 * is the token reader, and the `ColoriaTheme { }` composable (same name, different namespace)
 * installs the theme.
 */
object ColoriaTheme {

    /** Semantic colors of the (always light) Coloria theme. */
    val colors: ColoriaColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColoriaColors.current

    /** The eight approved text styles, all in Mulish. */
    val typography: ColoriaTextStyles
        @Composable
        @ReadOnlyComposable
        get() = LocalColoriaTextStyles.current
}
