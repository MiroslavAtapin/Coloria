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
import com.example.coloria.Screens.StartScreen
import com.example.coloria.Screens.WelcomeScreen
import com.example.coloria.ui.theme.ColoriaSpacing

enum class Screen {
    Start,
    Welcome,
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf(Screen.Start) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = ColoriaSpacing.screenHorizontal),
    ) {
        when (currentScreen) {
            Screen.Start -> StartScreen(
                onStartClick = { currentScreen = Screen.Welcome },
            )

            Screen.Welcome -> WelcomeScreen()
        }
    }
}
