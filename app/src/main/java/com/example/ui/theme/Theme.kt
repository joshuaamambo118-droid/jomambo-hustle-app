package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = JomamboPurpleLight,
    onPrimary = Color.Black,
    primaryContainer = JomamboPurpleDark,
    onPrimaryContainer = Color.White,
    secondary = JomamboGoldSecondary,
    onSecondary = Color.Black,
    secondaryContainer = JomamboGoldDark,
    onSecondaryContainer = Color.Black,
    tertiary = JomamboGreenEarn,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFD1C4E9),
    error = JomamboRedAlert
)

private val LightColorScheme = lightColorScheme(
    primary = JomamboPurplePrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEDE7F6),
    onPrimaryContainer = JomamboPurpleDark,
    secondary = JomamboGoldDark,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFFFF8E1),
    onSecondaryContainer = Color(0xFF5D4037),
    tertiary = JomamboGreenEarn,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Color(0xFF512DA8),
    error = JomamboRedAlert
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent JOMAMBO Gold & Purple branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
