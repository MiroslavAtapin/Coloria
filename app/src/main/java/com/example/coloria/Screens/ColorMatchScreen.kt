package com.example.coloria.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.coloria.Components.AppBar
import com.example.coloria.Components.Button
import com.example.coloria.Components.ColorPicker
import com.example.coloria.Components.GameCard
import com.example.coloria.Components.RecordCapsule
import com.example.coloria.ui.theme.ColoriaSpacing
import com.example.coloria.ui.theme.ColoriaTheme
import com.example.coloria.utils.CountdownTimer
import com.example.coloria.utils.StreakState
import com.example.coloria.utils.colorSimilarityPercent
import com.example.coloria.utils.logRoundColors
import com.example.coloria.utils.randomResultPhrase

private val ContentInset = ColoriaSpacing.xxl + ColoriaSpacing.lg

enum class ColorMatchStage {
    Memorize,
    Pick,
    Result,
}

@Composable
fun ColorMatchScreen(
    onHomeClick: () -> Unit,
    streak: StreakState,
) {
    var targetColor by remember { mutableStateOf(randomColor()) }
    var stage by remember { mutableStateOf(ColorMatchStage.Memorize) }
    var pickedColor by remember { mutableStateOf(Color.White) }
    var similarityPercent by remember { mutableStateOf<Int?>(null) }
    var showRecordCapsule by remember { mutableStateOf(false) }

    fun startNewRound() {
        targetColor = randomColor()
        pickedColor = Color.White
        similarityPercent = null
        showRecordCapsule = false
        stage = ColorMatchStage.Memorize
    }

    fun finishRound() {
        val percent = colorSimilarityPercent(targetColor, pickedColor)
        logRoundColors(target = targetColor, picked = pickedColor, similarityPercent = percent)
        streak.applyRound(percent)
        showRecordCapsule = streak.isRecordBeatenJustNow
        streak.consumeRecordBeaten()
        similarityPercent = percent
        stage = ColorMatchStage.Result
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppBar(
            soccer = streak.soccer,
            record = streak.record,
            isRecordBeaten = streak.isRecordBeaten,
            isRecordVisible = streak.isRecordLoaded,
            onLeftIconClick = onHomeClick,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = ColoriaSpacing.lg),
        ) {
            when (stage) {
                ColorMatchStage.Memorize -> GameCard(backgroundColor = targetColor)

                ColorMatchStage.Pick -> GameCard(backgroundColor = pickedColor)

                ColorMatchStage.Result -> GameCard(
                    backgroundColor = pickedColor,
                    splitColor = targetColor,
                    splitLabels = "Ваш\nвариант" to "Загаданный\nвариант",
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            when (stage) {
                ColorMatchStage.Memorize -> CountdownTimer(
                    seconds = 2,
                    onFinish = { stage = ColorMatchStage.Pick },
                )

                ColorMatchStage.Pick -> ColorPicker(
                    onColorChange = { pickedColor = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = ContentInset),
                )

                ColorMatchStage.Result -> ResultContent(
                    percent = similarityPercent ?: 0,
                    showRecordCapsule = showRecordCapsule,
                )
            }
        }

        if (stage != ColorMatchStage.Memorize) {
            Button(
                text = if (stage == ColorMatchStage.Pick) "Проверить" else "Ещё раз",
                onClick = {
                    if (stage == ColorMatchStage.Pick) {
                        finishRound()
                    } else {
                        startNewRound()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = ContentInset,
                        end = ContentInset,
                        top = ColoriaSpacing.sm,
                        bottom = ColoriaSpacing.lg,
                    ),
            )
        }
    }
}

@Composable
private fun ResultContent(
    percent: Int,
    showRecordCapsule: Boolean,
) {
    val phrase = remember(percent) { randomResultPhrase(percent) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ColoriaSpacing.lg),
    ) {
        if (showRecordCapsule) {
            RecordCapsule()
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ColoriaSpacing.sm),
        ) {
            Text(
                text = "$percent%",
                style = ColoriaTheme.typography.headlineLarge,
                color = ColoriaTheme.colors.textPrimary,
            )

            Text(
                text = phrase,
                style = ColoriaTheme.typography.bodyLarge,
                color = ColoriaTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = ColoriaSpacing.lg),
            )
        }
    }
}

private fun randomColor(): Color = Color(
    red = (0..255).random(),
    green = (0..255).random(),
    blue = (0..255).random(),
)

@Preview(showBackground = true)
@Composable
fun ColorMatchPreview() {
    ColorMatchScreen(
        onHomeClick = {},
        streak = remember { StreakState() },
    )
}