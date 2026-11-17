package com.example.coloria

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.coloria.Screens.ColorMatchScreen
import com.example.coloria.Screens.ColorSpotScreen
import com.example.coloria.Screens.HomeScreen
import com.example.coloria.Screens.WelcomeScreen
import com.example.coloria.ui.theme.ColoriaSpacing
import com.example.coloria.utils.COLOR_SPOT_RECORD_KEY
import com.example.coloria.utils.DataStoreOnboardingStorage
import com.example.coloria.utils.DataStoreStreakStorage
import com.example.coloria.utils.StreakState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

enum class Screen {
    Welcome,
    Home,
    ColorMatch,
    ColorSpot,
}

@Composable
fun AppNavigation() {
    val context = LocalContext.current.applicationContext
    val storage = remember { DataStoreOnboardingStorage(context) }
    val scope = rememberCoroutineScope()

    var isWelcomeShown by remember { mutableStateOf<Boolean?>(null) }
    var currentScreen by remember { mutableStateOf(Screen.Home) }

    LaunchedEffect(Unit) {
        storage.isWelcomeShownFlow.collect { isShown ->
            if (isWelcomeShown == null) {
                isWelcomeShown = isShown
                if (!isShown) currentScreen = Screen.Welcome
            }
        }
    }

    // Стрик и рекорд живут выше экрана игры, поэтому не сбрасываются при выходе на главную.
    // DataStore требует долгоживущий scope — remember даёт область на всё время жизни навигации.
    val streak = remember {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        StreakState(storage = DataStoreStreakStorage(context), scope = scope)
    }

    // У второй игры свой счёт и свой рекорд — отдельный ключ в том же DataStore,
    // иначе результат одной игры подменял бы рекорд другой.
    val spotStreak = remember {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        StreakState(
            storage = DataStoreStreakStorage(context, COLOR_SPOT_RECORD_KEY),
            scope = scope,
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = ColoriaSpacing.screenHorizontal),
    ) {
        if (isWelcomeShown == null) return@Box

        when (currentScreen) {
            Screen.Welcome -> WelcomeScreen(
                onAcceptClick = {
                    currentScreen = Screen.Home
                    scope.launch { storage.markWelcomeShown() }
                },
            )

            Screen.Home -> HomeScreen(
                onGameOneClick = { currentScreen = Screen.ColorMatch },
                onGameTwoClick = { currentScreen = Screen.ColorSpot },
            )

            Screen.ColorMatch -> ColorMatchScreen(
                onHomeClick = { currentScreen = Screen.Home },
                streak = streak,
            )

            Screen.ColorSpot -> ColorSpotScreen(
                onHomeClick = { currentScreen = Screen.Home },
                streak = spotStreak,
            )
        }
    }
}
