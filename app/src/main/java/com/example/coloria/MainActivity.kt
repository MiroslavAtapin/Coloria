package com.example.coloria

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.coloria.ui.theme.ColoriaTheme

class MainActivity : ComponentActivity() {


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase.createLightConfigurationContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(TRANSPARENT, TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(TRANSPARENT, TRANSPARENT),
        )

        setContent {
            ColoriaTheme {
                AppNavigation()
            }
        }
    }


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


private fun Configuration.asLightConfiguration(): Configuration =
    Configuration(this).apply {
        uiMode = (uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or
            Configuration.UI_MODE_NIGHT_NO
    }


private fun Context.createLightConfigurationContext(): Context =
    createConfigurationContext(resources.configuration.asLightConfiguration())

