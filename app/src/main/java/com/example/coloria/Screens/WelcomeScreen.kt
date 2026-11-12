package com.example.coloria.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coloria.Components.Button
import com.example.coloria.R
import com.example.coloria.ui.theme.ColoriaSpacing
import com.example.coloria.ui.theme.ColoriaTheme

@Composable
fun WelcomeScreen(onAcceptClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
        )

        Column(
            modifier = Modifier.padding(top = ColoriaSpacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ColoriaSpacing.sm),
        ) {
            Text(
                text = "Добро пожаловать!",
                style = ColoriaTheme.typography.headlineSmall,
                color = ColoriaTheme.colors.textPrimary,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem",
                style = ColoriaTheme.typography.bodyLarge,
                color = ColoriaTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ColoriaSpacing.lg),
            )
        }

        Button(
            text = "Принять",
            onClick = onAcceptClick,
            modifier = Modifier.padding(top = ColoriaSpacing.xxl),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview(){
    WelcomeScreen(onAcceptClick = {})
}