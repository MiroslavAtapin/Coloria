package com.example.coloria

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.coloria.Screens.ColorMatchScreen
import com.example.coloria.Screens.HomeScreen
import com.example.coloria.ui.theme.ColoriaSpacing

enum class Screen {
    Home,
    ColorMatch,
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf(Screen.Home) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = ColoriaSpacing.screenHorizontal),
    ) {
        when (currentScreen) {
            Screen.Home -> HomeScreen(
                onGameOneClick = { currentScreen = Screen.ColorMatch },
                onGameTwoClick = { },
            )

            Screen.ColorMatch -> ColorMatchScreen(
                onHomeClick = { currentScreen = Screen.Home },
            )
        }
    }
}
