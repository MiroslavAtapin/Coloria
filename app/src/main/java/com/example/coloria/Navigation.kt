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
import androidx.compose.ui.platform.LocalContext
import com.example.coloria.Screens.ColorMatchScreen
import com.example.coloria.Screens.HomeScreen
import com.example.coloria.ui.theme.ColoriaSpacing
import com.example.coloria.utils.DataStoreStreakStorage
import com.example.coloria.utils.StreakState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

enum class Screen {
    Home,
    ColorMatch,
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf(Screen.Home) }
    val context = LocalContext.current.applicationContext

    // Стрик и рекорд живут выше экрана игры, поэтому не сбрасываются при выходе на главную.
    // DataStore требует долгоживущий scope — remember даёт область на всё время жизни навигации.
    val streak = remember {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        StreakState(storage = DataStoreStreakStorage(context), scope = scope)
    }

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
                streak = streak,
            )
        }
    }
}
