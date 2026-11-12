package com.example.coloria.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.coloria.Components.Button
import com.example.coloria.ui.theme.ColoriaTheme

@Composable
fun StartScreen(onStartClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColoriaTheme.colors.backgroundPrimary),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            text = "Начать",
            onClick = onStartClick,
        )
    }
}
