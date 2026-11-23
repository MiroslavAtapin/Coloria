package com.example.coloria.Screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.coloria.R
import com.example.coloria.ui.theme.ColoriaShadowRenderer
import com.example.coloria.ui.theme.ColoriaShadows
import com.example.coloria.ui.theme.ColoriaSpacing
import com.example.coloria.ui.theme.ColoriaTheme

private const val CardAspectRatio = 338f / 200f

private val CardCornerRadius = 24.dp

private val MaxCardWidth = 420.dp

private val HomeHorizontalInset = ColoriaSpacing.lg

private val CardGap = ColoriaSpacing.lg

private const val TextBlockWidthFraction = 0.5f

private val TitleToCardsGap = 80.dp

@Composable
fun HomeScreen(
    onGameOneClick: () -> Unit,
    onGameTwoClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = HomeHorizontalInset),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = ColoriaTheme.typography.headlineLarge,
            color = ColoriaTheme.colors.textPrimary,
        )

        Spacer(modifier = Modifier.height(TitleToCardsGap))

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false),
        ) {
            val widthLimit = minOf(maxWidth, MaxCardWidth)
            val heightByWidth = widthLimit / CardAspectRatio
            val heightByHeight = ((maxHeight - CardGap) / 2f).coerceAtLeast(0.dp)
            val cardHeight = minOf(heightByWidth, heightByHeight)
            val cardWidth = cardHeight * CardAspectRatio

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(CardGap),
            ) {
                GameEntryCard(
                    width = cardWidth,
                    height = cardHeight,
                    backgroundRes = R.drawable.back_card_1,
                    title = "Подбери цвет",
                    description = "Насколько близко ты попадёшь в цвет?",
                    onClick = onGameOneClick,
                )

                GameEntryCard(
                    width = cardWidth,
                    height = cardHeight,
                    backgroundRes = R.drawable.back_card_2,
                    title = "Найди отличие",
                    description = "Один оттенок выбивается из ряда",
                    onClick = onGameTwoClick,
                )
            }
        }
    }
}

@Composable
private fun GameEntryCard(
    width: Dp,
    height: Dp,
    @DrawableRes backgroundRes: Int,
    title: String,
    description: String,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(CardCornerRadius)

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .drawBehind {
                ColoriaShadowRenderer.run {
                    drawShadow(
                        size = size,
                        cornerRadiusPx = CardCornerRadius.toPx(),
                        shadow = ColoriaShadows.Small,
                        shapeColor = Color.Transparent,
                    )
                }
            }
            .clip(shape)
            .clickable(role = Role.Button, onClick = onClick),
    ) {
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize(),
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth(TextBlockWidthFraction)
                .padding(horizontal = ColoriaSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(ColoriaSpacing.xs),
        ) {
            Text(
                text = title,
                style = ColoriaTheme.typography.titleLarge,
                color = ColoriaTheme.colors.textPrimary,
            )
            Text(
                text = description,
                style = ColoriaTheme.typography.bodyMedium,
                color = ColoriaTheme.colors.textSecondary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview(){
    HomeScreen(
        onGameOneClick = {}
    ) { }
}
