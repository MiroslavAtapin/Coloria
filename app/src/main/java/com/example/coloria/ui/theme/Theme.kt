package com.example.coloria.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color


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
    scrim = Color.Black,
    inverseSurface = ColoriaPalette.TextPrimary,
    inverseOnSurface = ColoriaPalette.BackgroundPrimary,
    inversePrimary = ColoriaPalette.AccentSecondary,
)


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


object ColoriaTheme {


    val colors: ColoriaColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColoriaColors.current


    val typography: ColoriaTextStyles
        @Composable
        @ReadOnlyComposable
        get() = LocalColoriaTextStyles.current
}
