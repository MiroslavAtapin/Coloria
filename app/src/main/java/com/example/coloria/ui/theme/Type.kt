package com.example.coloria.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.coloria.R

/**
 * Mulish — the app's only typeface.
 *
 * The .ttf files live in `app/src/main/res/font/` (`mulish_bold.ttf`, `mulish_medium.ttf`,
 * `mulish_regular.ttf`). They are wired as separate weight slots (not as `FontFamily(Font(...))`
 * with a `weight` argument) because the bundled files already contain their final weights —
 * this keeps rendering identical to Figma and avoids synthetic bolding.
 */
val MulishFamily = FontFamily(
    fonts = listOf(
        Font(R.font.mulish_regular, weight = FontWeight.Normal),
        Font(R.font.mulish_medium, weight = FontWeight.Medium),
        Font(R.font.mulish_bold, weight = FontWeight.Bold),
    )
)

// --- Design-system text styles (approved scale, do not invent new sizes) ---------

/** 32 / 40, Bold */
val HeadlineLargeStyle = TextStyle(
    fontFamily = MulishFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp,
    lineHeight = 40.sp,
)

/** 24 / 32, Medium */
val HeadlineSmallStyle = TextStyle(
    fontFamily = MulishFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 24.sp,
    lineHeight = 32.sp,
)

/** 20 / 28, Bold */
val TitleLargeStyle = TextStyle(
    fontFamily = MulishFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    lineHeight = 28.sp,
)

/** 16 / 24, Medium */
val TitleMediumStyle = TextStyle(
    fontFamily = MulishFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    lineHeight = 24.sp,
)

/** 14 / 20, Bold — the compact emphasized style ("title small-super"). */
val TitleSmallSuperStyle = TextStyle(
    fontFamily = MulishFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 14.sp,
    lineHeight = 20.sp,
)

/** 16 / 24, Regular */
val BodyLargeStyle = TextStyle(
    fontFamily = MulishFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
)

/** 14 / 20, Regular */
val BodyMediumStyle = TextStyle(
    fontFamily = MulishFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
)

/** 12 / 16, Regular */
val BodySmallStyle = TextStyle(
    fontFamily = MulishFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 16.sp,
)

/**
 * Material [Typography] mapping.
 *
 * Only the roles that exist in the design scale are filled; every other Material role
 * (labels, display, etc.) is intentionally pointed at the design styles too, so that any
 * Material component used later still renders with Mulish and the approved sizes instead of
 * slipping back to the platform default font/sizes.
 */
val ColoriaTypography = Typography(
    // Design scale ----------------------------------------------------------
    headlineLarge = HeadlineLargeStyle,          // 32 / 40 bold
    headlineSmall = HeadlineSmallStyle,          // 24 / 32 medium
    titleLarge = TitleLargeStyle,                // 20 / 28 bold
    titleMedium = TitleMediumStyle,              // 16 / 24 medium
    titleSmall = TitleSmallSuperStyle,           // 14 / 20 bold
    bodyLarge = BodyLargeStyle,                  // 16 / 24 regular
    bodyMedium = BodyMediumStyle,                // 14 / 20 regular
    bodySmall = BodySmallStyle,                  // 12 / 16 regular

    // Remaining Material roles mapped onto the design scale (no new sizes) ---
    displayLarge = HeadlineLargeStyle,
    displayMedium = HeadlineLargeStyle,
    displaySmall = HeadlineSmallStyle,
    headlineMedium = HeadlineSmallStyle,
    labelLarge = TitleMediumStyle,
    labelMedium = BodyMediumStyle,
    labelSmall = BodySmallStyle,
)
