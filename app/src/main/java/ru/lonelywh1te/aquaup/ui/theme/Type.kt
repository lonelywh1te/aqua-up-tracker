package ru.lonelywh1te.aquaup.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import ru.lonelywh1te.aquaup.R

val OnestFontFamily = FontFamily(
    Font(
        resId = R.font.onest_regular,
        weight = FontWeight.Normal,
    ),
    Font(
        resId = R.font.onest_medium,
        weight = FontWeight.Medium,
    ),
    Font(
        resId = R.font.onest_semibold,
        weight = FontWeight.SemiBold,
    ),
    Font(
        resId = R.font.onest_bold,
        weight = FontWeight.Bold,
    ),
)

val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = OnestFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = OnestFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = OnestFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = OnestFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = OnestFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = OnestFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = OnestFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = OnestFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = OnestFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = OnestFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = OnestFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = OnestFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = OnestFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = OnestFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = OnestFontFamily),
)