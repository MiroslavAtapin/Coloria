package com.example.coloria.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.coloria.ui.theme.ColoriaRadii
import com.example.coloria.ui.theme.ColoriaShadowRenderer
import com.example.coloria.ui.theme.ColoriaShadows
import com.example.coloria.ui.theme.ColoriaTheme

@Composable
fun IconButton(
    iconResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    val backgroundColor = ColoriaTheme.colors.backgroundSurface
    val shape = CircleShape

    val iconPainter: Painter = painterResource(id = iconResId)

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
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .clip(shape)
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        androidx.compose.material3.Icon(
            painter = iconPainter,
            contentDescription = contentDescription,
            tint = ColoriaTheme.colors.textPrimary,
            modifier = Modifier.size(24.dp),
        )
    }
}