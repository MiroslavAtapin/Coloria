package com.example.coloria.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.coloria.R
import com.example.coloria.ui.theme.ColoriaSpacing
import com.example.coloria.ui.theme.ColoriaTheme

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    number: String = "1240",
    label: String = "очки",
    onLeftIconClick: () -> Unit = {},
    onRightIconClick: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            iconResId = R.drawable.ic_home,
            onClick = onLeftIconClick,
            contentDescription = "Главная",
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = number,
                style = ColoriaTheme.typography.titleLarge,
                color = ColoriaTheme.colors.textPrimary,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(ColoriaSpacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cup),
                    contentDescription = null,
                    tint = ColoriaTheme.colors.textSecondary,
                    modifier = Modifier.size(12.dp),
                )

                Text(
                    text = label,
                    style = ColoriaTheme.typography.bodySmall,
                    color = ColoriaTheme.colors.textSecondary,
                )
            }
        }

        IconButton(
            iconResId = R.drawable.ic_setting,
            onClick = onRightIconClick,
            contentDescription = "Настройки",
        )
    }
}
