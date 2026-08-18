package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val UnresolvedDarkColorScheme = darkColorScheme(
    primary = AccentRed,
    onPrimary = TextPrimary,
    primaryContainer = AccentRedDark,
    onPrimaryContainer = TextPrimary,
    secondary = AccentAmber,
    onSecondary = BackgroundDark,
    tertiary = AccentCyan,
    onTertiary = BackgroundDark,
    background = BackgroundDark,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceBorder
)

@Composable
fun UnresolvedTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = UnresolvedDarkColorScheme,
        typography = Typography,
        content = content
    )
}
