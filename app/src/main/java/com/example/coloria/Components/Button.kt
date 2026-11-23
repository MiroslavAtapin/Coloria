package com.example.coloria.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.coloria.ui.theme.ColoriaRadii
import com.example.coloria.ui.theme.ColoriaShadowRenderer
import com.example.coloria.ui.theme.ColoriaShadows
import com.example.coloria.ui.theme.ColoriaTheme
import com.example.coloria.utils.LocalSoundPlayer

@Composable
fun Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = ColoriaTheme.colors.accentPrimary
    val shape = RoundedCornerShape(ColoriaRadii.Maximal)
    val soundPlayer = LocalSoundPlayer.current

    Box(
        modifier = modifier
            .drawBehind {
                ColoriaShadowRenderer.run {
                    drawShadow(
                        size = size,
                        cornerRadiusPx = ColoriaRadii.Maximal.toPx(),
                        shadow = ColoriaShadows.Small,
                        shapeColor = backgroundColor,
                    )
                }
            }
            .background(backgroundColor, shape)
            .defaultMinSize(minHeight = 48.dp)
            .clip(shape)
            .clickable(
                role = Role.Button,
                onClick = {
                    soundPlayer.playSelect()
                    onClick()
                },
            )
            .padding(PaddingValues(horizontal = 56.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = ColoriaTheme.typography.titleMedium,
            color = ColoriaTheme.colors.onAccent,
            textAlign = TextAlign.Center,
        )
    }
}
