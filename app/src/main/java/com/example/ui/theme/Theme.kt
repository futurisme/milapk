package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val TacticalMilitaryColorScheme = darkColorScheme(
    primary = MilitaryTacticalGreen,
    onPrimary = MilitaryBlack,
    primaryContainer = MilitaryElevatedSurface,
    onPrimaryContainer = MilitaryTacticalGreen,
    secondary = MilitaryGold,
    onSecondary = MilitaryBlack,
    secondaryContainer = MilitaryElevatedSurface,
    onSecondaryContainer = MilitaryBrass,
    tertiary = MilitaryAlertAmber,
    onTertiary = MilitaryBlack,
    background = MilitaryBlack,
    onBackground = MilitaryTextPrimary,
    surface = MilitaryDarkSurface,
    onSurface = MilitaryTextPrimary,
    surfaceVariant = MilitaryCardSurface,
    onSurfaceVariant = MilitaryTextSecondary,
    outline = MilitaryBorder,
    error = MilitaryHostileRed
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TacticalMilitaryColorScheme,
        typography = Typography,
        content = content
    )
}
