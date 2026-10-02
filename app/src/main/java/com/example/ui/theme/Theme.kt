package com.jomambo.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// JOMAMBO BRAND COLORS - ALL DEFINED HERE!
val JomamboPurplePrimary = Color(0xFF6A1B9A)
val JomamboPurpleLight = Color(0xFF9C4DCC)
val JomamboPurpleDark = Color(0xFF38006B)
val JomamboGoldPrimary = Color(0xFFFFC107)
val JomamboGoldLight = Color(0xFFFFF350)
val JomamboGoldSecondary = Color(0xFFFFCA28)
val JomamboGoldDark = Color(0xFFC49000)
val JomamboGreenEarn = Color(0xFF2E7D32)
val JomamboRedAlert = Color(0xFFD32F2F)

// Backgrounds
val LightBackground = Color(0xFFFFFBFE)
val LightOnBackground = Color(0xFF1C1B1F)
val LightSurface = Color(0xFFFFFBFE)
val LightOnSurface = Color(0xFF1C1B1F)
val LightSurfaceVariant = Color(0xFFE7E0EC)

val DarkBackground = Color(0xFF121212)
val DarkOnBackground = Color(0xFFE6E1E5)
val DarkSurface = Color(0xFF121212)
val DarkOnSurface = Color(0xFFE6E1E5)
val DarkSurfaceVariant = Color(0xFF49454F)

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

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

@Composable
fun JomamboTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
