package com.example.coloria.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.coloria.R
import com.example.coloria.ui.theme.ColoriaRadii
import com.example.coloria.ui.theme.ColoriaShadowRenderer
import com.example.coloria.ui.theme.ColoriaShadows
import com.example.coloria.ui.theme.ColoriaSpacing
import com.example.coloria.ui.theme.ColoriaTheme


@Composable
fun RecordCapsule(
    modifier: Modifier = Modifier,
    text: String = "Новый рекорд!",
) {
    val shape = RoundedCornerShape(ColoriaRadii.Maximal)
    val background = ColoriaTheme.colors.stateGoodSoft

    Row(
        modifier = modifier
            .drawBehind {
                ColoriaShadowRenderer.run {
                    drawShadow(
                        size = size,
                        cornerRadiusPx = ColoriaRadii.Maximal.toPx(),
                        shadow = ColoriaShadows.Small,
                        shapeColor = background,
                    )
                }
            }
            .background(background, shape)
            .clip(shape)
            .padding(
                horizontal = ColoriaSpacing.lg,
                vertical = ColoriaSpacing.sm,
            ),
        horizontalArrangement = Arrangement.spacedBy(ColoriaSpacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_cup),
            contentDescription = null,
            tint = ColoriaTheme.colors.stateGood,
            modifier = Modifier.size(16.dp),
        )

        Text(
            text = text,
            style = ColoriaTheme.typography.titleSmallSuper,
            color = ColoriaTheme.colors.stateGood,
        )
    }
}
