package com.example.coloria.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coloria.Components.AppBar
import com.example.coloria.Components.Button
import com.example.coloria.Components.RecordCapsule
import com.example.coloria.ui.theme.ColoriaRadii
import com.example.coloria.ui.theme.ColoriaShadowRenderer
import com.example.coloria.ui.theme.ColoriaShadows
import com.example.coloria.ui.theme.ColoriaShapes
import com.example.coloria.ui.theme.ColoriaSpacing
import com.example.coloria.ui.theme.ColoriaTheme
import com.example.coloria.utils.StreakState
import com.example.coloria.utils.colorSimilarityPercent
import kotlin.math.abs
import kotlin.random.Random

private val TileSize = 72.dp
private val TileGap = 10.dp
private const val GridSize = 3
private val GridWidth = TileSize * GridSize + TileGap * (GridSize - 1)
private val ResultBorderWidth = 4.dp
private const val WIN_PERCENT = 100
private const val LOSE_PERCENT = 0
private const val ColorSampleSteps = 96

enum class ColorSpotStage {
    Playing,
    Lost,
}

@Composable
fun ColorSpotScreen(
    onHomeClick: () -> Unit,
    streak: StreakState,
) {
    var round by remember { mutableStateOf(1) }
    var baseColor by remember { mutableStateOf(randomBaseColor()) }
    var spotColor by remember { mutableStateOf(spotColorFor(baseColor, randomSimilarityForRound(1))) }
    var spotIndex by remember { mutableStateOf(randomSpotIndex()) }
    var stage by remember { mutableStateOf(ColorSpotStage.Playing) }
    var pickedIndex by remember { mutableStateOf<Int?>(null) }
    var showRecordCapsule by remember { mutableStateOf(false) }

    fun startRound(roundNumber: Int) {
        round = roundNumber
        baseColor = randomBaseColor()
        spotColor = spotColorFor(baseColor, randomSimilarityForRound(roundNumber))
        spotIndex = randomSpotIndex()
        pickedIndex = null
        stage = ColorSpotStage.Playing
    }

    fun nextRound() {
        startRound(round + 1)
    }

    fun restartGame() {
        startRound(1)
        showRecordCapsule = false
    }

    fun onTileClick(index: Int) {
        if (stage != ColorSpotStage.Playing) return

        if (index == spotIndex) {
            streak.applyRound(WIN_PERCENT)
            showRecordCapsule = streak.isRecordBeatenJustNow
            streak.consumeRecordBeaten()
            nextRound()
        } else {
            streak.applyRound(LOSE_PERCENT)
            pickedIndex = index
            stage = ColorSpotStage.Lost
        }
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = ColoriaSpacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val isLost = stage == ColorSpotStage.Lost

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(ColoriaSpacing.sm),
                ) {
                    if (isLost) {
                        Text(
                            text = "Игра окончена",
                            style = ColoriaTheme.typography.titleLarge,
                            color = ColoriaTheme.colors.textPrimary,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = "Попробуешь снова?",
                            style = ColoriaTheme.typography.bodyMedium,
                            color = ColoriaTheme.colors.textSecondary,
                            textAlign = TextAlign.Center,
                        )
                    }

                    if (showRecordCapsule) {
                        RecordCapsule()
                    }
                }
            }

            ColorSpotGrid(
                baseColor = baseColor,
                spotColor = spotColor,
                spotIndex = spotIndex,
                pickedIndex = pickedIndex,
                isRevealed = isLost,
                onTileClick = ::onTileClick,
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                if (isLost) {
                    Button(
                        text = "Ещё раз",
                        onClick = ::restartGame,
                        modifier = Modifier.width(GridWidth),
                    )
                }
            }
        }
    }
}
@Composable
private fun ColorSpotGrid(
    baseColor: Color,
    spotColor: Color,
    spotIndex: Int,
    pickedIndex: Int?,
    isRevealed: Boolean,
    onTileClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(TileGap),
    ) {
        repeat(GridSize) { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(TileGap)) {
                repeat(GridSize) { column ->
                    val index = row * GridSize + column

                    val borderColor = when {
                        isRevealed && index == spotIndex -> ColoriaTheme.colors.stateGood
                        isRevealed && index == pickedIndex -> ColoriaTheme.colors.stateBad
                        else -> null
                    }

                    ColorSpotTile(
                        color = if (index == spotIndex) spotColor else baseColor,
                        borderColor = borderColor,
                        isClickable = !isRevealed,
                        onClick = { onTileClick(index) },
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorSpotTile(
    color: Color,
    borderColor: Color?,
    isClickable: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(TileSize)
            .drawBehind {
                ColoriaShadowRenderer.run {
                    drawShadow(
                        size = size,
                        cornerRadiusPx = ColoriaRadii.Small.toPx(),
                        shadow = ColoriaShadows.Small,
                        shapeColor = color,
                    )
                }
            }
            .background(color, ColoriaShapes.Small)
            .clip(ColoriaShapes.Small)
            .then(
                if (borderColor != null) {
                    Modifier.border(ResultBorderWidth, borderColor, ColoriaShapes.Small)
                } else {
                    Modifier
                },
            )
            .clickable(enabled = isClickable, role = Role.Button, onClick = onClick),
    )
}

private fun randomSpotIndex(): Int = (0 until GridSize * GridSize).random()

private fun similarityRangeForRound(round: Int): IntRange = when {
    round <= 3 -> 76..80
    round <= 6 -> 82..86
    round <= 9 -> 88..92
    round <= 12 -> 93..96
    else -> 96..98
}

private fun randomSimilarityForRound(round: Int): Int = similarityRangeForRound(round).random()

private fun randomBaseColor(): Color {
    val hue = (0 until 360).random().toFloat()
    val saturation = 0.35f + Random.nextFloat() * 0.45f
    val value = 0.55f + Random.nextFloat() * 0.30f

    return Color.hsv(hue, saturation, value)
}

private fun spotColorFor(base: Color, targetSimilarity: Int): Color {
    var best = base
    var bestGap = Int.MAX_VALUE

    for (channel in 0..2) {
        for (direction in 0..1) {
            val sign = if (direction == 0) 1f else -1f

            for (step in 1..ColorSampleSteps) {
                val delta = sign * step * 255f / ColorSampleSteps
                val candidate = shiftedChannelColor(base, channel, delta)
                val gap = abs(colorSimilarityPercent(base, candidate) - targetSimilarity)

                if (gap < bestGap) {
                    bestGap = gap
                    best = candidate
                }
            }
        }
    }

    return best
}

private fun shiftedChannelColor(base: Color, channel: Int, offset: Float): Color {
    val delta = offset / 255f

    return when (channel) {
        0 -> base.copy(red = (base.red + delta).coerceIn(0f, 1f))
        1 -> base.copy(green = (base.green + delta).coerceIn(0f, 1f))
        else -> base.copy(blue = (base.blue + delta).coerceIn(0f, 1f))
    }
}

@Preview(showBackground = true)
@Composable
fun ColorSpotPreview() {
    ColorSpotScreen(
        onHomeClick = {},
        streak = remember { StreakState() },
    )
}
