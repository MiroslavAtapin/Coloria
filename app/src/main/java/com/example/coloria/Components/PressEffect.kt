package com.example.coloria.Components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role

@Composable
fun Modifier.pressEffect(
    enabled: Boolean = true,
    pressedScale: Float = 0.96f,
    role: Role? = Role.Button,
    onClick: () -> Unit,
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (enabled && isPressed) pressedScale else 1f,
        animationSpec = tween(
            durationMillis = if (enabled && isPressed) 110 else 165,
            easing = FastOutSlowInEasing,
        ),
        label = "pressScale",
    )
    val alpha by animateFloatAsState(
        targetValue = if (enabled && isPressed) 0.94f else 1f,
        animationSpec = tween(
            durationMillis = if (enabled && isPressed) 110 else 165,
            easing = FastOutSlowInEasing,
        ),
        label = "pressAlpha",
    )

    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
            this.alpha = alpha
        }
        .clickable(
            enabled = enabled,
            interactionSource = interactionSource,
            indication = null,
            role = role,
            onClick = onClick,
        )
}
