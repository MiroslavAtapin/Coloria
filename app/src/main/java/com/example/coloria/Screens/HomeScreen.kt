package com.example.coloria.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.coloria.Components.Button
import com.example.coloria.ui.theme.ColoriaSpacing

@Composable
fun HomeScreen(
    onGameOneClick: () -> Unit,
    onGameTwoClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ColoriaSpacing.lg, Alignment.CenterVertically),
    ) {
        Button(
            text = "Игра 1",
            onClick = onGameOneClick,
        )

        Button(
            text = "Игра 2",
            onClick = onGameTwoClick,
        )
    }
}
