package com.example.coloria

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.coloria.Components.AppBar
import com.example.coloria.Components.Button
import com.example.coloria.Components.IconButton
import com.example.coloria.ui.theme.ColoriaSpacing
import com.example.coloria.ui.theme.ColoriaTheme

class MainActivity : ComponentActivity() {

    /**
     * Forces the activity's resources into **light** mode before the theme is applied.
     *
     * This is what makes the app truly immune to the device's dark-theme setting: the night bit
     * of `uiMode` is cleared on the configuration this activity inflates with, so no resource
     * qualifier (`-night`), platform widget or system component can switch to a dark variant.
     * Dependency-free (unlike `AppCompatDelegate`), and it also applies to the splash/launch
     * window because it runs before `onCreate`.
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase.createLightConfigurationContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Light-only system bars: dark icons over our light palette, never night-styled.
        // (Portrait lock is enforced declaratively in the manifest.)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(TRANSPARENT, TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(TRANSPARENT, TRANSPARENT),
        )

        setContent {
            ColoriaTheme {
                StartScreen()
            }
        }
    }

    /**
     * Re-asserts light mode if the system configuration changes at runtime. The activity
     * declares `uiMode` in the manifest, so this is handled without recreating the activity and
     * without interrupting an ongoing game/mini-game.
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (isNightModeActive(newConfig)) {
            resources.updateConfiguration(
                resources.configuration.asLightConfiguration(),
                resources.displayMetrics,
            )
        }
    }

    private fun isNightModeActive(config: Configuration): Boolean =
        (config.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

    private companion object {
        const val TRANSPARENT = android.graphics.Color.TRANSPARENT
    }
}

/** Returns a copy of this configuration with the night bit cleared (always light). */
private fun Configuration.asLightConfiguration(): Configuration =
    Configuration(this).apply {
        uiMode = (uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or
            Configuration.UI_MODE_NIGHT_NO
    }

/** Creates a context whose resources are permanently in light mode. */
private fun Context.createLightConfigurationContext(): Context =
    createConfigurationContext(resources.configuration.asLightConfiguration())

/**
 * Временный стартовый экран. Показывает фон и заголовок из дизайн-системы.
 * Заменить на реальный экран приложения.
 */
@Composable
private fun StartScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColoriaTheme.colors.backgroundPrimary),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AppBar(

            )
            IconButton(
                onClick = {},
                iconResId = R.drawable.ic_cup
            )
            Button(
                onClick = {},
                text = "Label"
            )
        }
    }
}

