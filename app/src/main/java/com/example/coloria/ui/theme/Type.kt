package com.example.coloria.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.coloria.R


val MulishFamily = FontFamily(
    fonts = listOf(
        Font(R.font.mulish_regular, weight = FontWeight.Normal),
        Font(R.font.mulish_medium, weight = FontWeight.Medium),
        Font(R.font.mulish_bold, weight = FontWeight.Bold),
    )
)


val HeadlineLargeStyle = TextStyle(
    fontFamily = MulishFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp,
    lineHeight = 40.sp,
)


val HeadlineSmallStyle = TextStyle(
    fontFamily = MulishFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
    lineHeight = 32.sp,
)


val TitleLargeStyle = TextStyle(
    fontFamily = MulishFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    lineHeight = 28.sp,
)


val TitleMediumStyle = TextStyle(
    fontFamily = MulishFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    lineHeight = 24.sp,
)


val TitleSmallSuperStyle = TextStyle(
    fontFamily = MulishFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 14.sp,
    lineHeight = 20.sp,
)


val BodyLargeStyle = TextStyle(
    fontFamily = MulishFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
)


val BodyMediumStyle = TextStyle(
    fontFamily = MulishFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
)


val BodySmallStyle = TextStyle(
    fontFamily = MulishFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 16.sp,
)


val ColoriaTypography = Typography(
    headlineLarge = HeadlineLargeStyle,
    headlineSmall = HeadlineSmallStyle,
    titleLarge = TitleLargeStyle,
    titleMedium = TitleMediumStyle,
    titleSmall = TitleSmallSuperStyle,
    bodyLarge = BodyLargeStyle,
    bodyMedium = BodyMediumStyle,
    bodySmall = BodySmallStyle,
    displayLarge = HeadlineLargeStyle,
    displayMedium = HeadlineLargeStyle,
    displaySmall = HeadlineSmallStyle,
    headlineMedium = HeadlineSmallStyle,
    labelLarge = TitleMediumStyle,
    labelMedium = BodyMediumStyle,
    labelSmall = BodySmallStyle,
)
