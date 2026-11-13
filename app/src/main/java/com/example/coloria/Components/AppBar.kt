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

/**
 * Шапка экрана: число по центру, иконки по краям.
 *
 * @param soccer текущий стрик — крупное число по центру.
 * @param record лучший результат — подпись под числом.
 * @param isRecordBeaten рекорд побит: подпись скрывается, остаётся только [soccer].
 * @param isRecordVisible показывать ли подпись рекорда; `false`, пока он ещё не прочитан с устройства.
 */
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    soccer: Int = 0,
    record: Int = 0,
    isRecordBeaten: Boolean = false,
    isRecordVisible: Boolean = true,
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
                text = soccer.toString(),
                style = ColoriaTheme.typography.titleLarge,
                color = if (isRecordBeaten) {
                    ColoriaTheme.colors.accentPrimary
                } else {
                    ColoriaTheme.colors.textPrimary
                },
            )

            if (isRecordVisible && !isRecordBeaten) {
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
                        text = "$record",
                        style = ColoriaTheme.typography.bodySmall,
                        color = ColoriaTheme.colors.textSecondary,
                    )
                }
            }
        }

        IconButton(
            iconResId = R.drawable.ic_setting,
            onClick = onRightIconClick,
            contentDescription = "Настройки",
        )
    }
}
