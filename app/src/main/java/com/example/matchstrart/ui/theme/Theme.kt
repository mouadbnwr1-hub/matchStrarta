package com.example.matchstrart.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color


private val LightPrimary = Color(0xFF1976D2)
private val LightOnPrimary = Color.White
private val LightPrimaryContainer = Color(0xFF90CAF9)
private val LightSecondary = Color(0xFF388E3C)
private val LightOnSecondary = Color.White
private val LightBackground = Color(0xFFFAFAFA)
private val LightSurface = Color.White
private val LightError = Color(0xFFB00020)

// Couleurs Mode Sombre
private val DarkPrimary = Color(0xFF64B5F6)
private val DarkOnPrimary = Color(0xFF001D35)
private val DarkPrimaryContainer = Color(0xFF42A5F5)
private val DarkSecondary = Color(0xFF81C784)
private val DarkOnSecondary = Color(0xFF003910)
private val DarkBackground = Color(0xFF121212)
private val DarkSurface = Color(0xFF1E1E1E)
private val DarkError = Color(0xFFCF6679)

// Couleurs de statut
val StatusAvailable = Color(0xFF4CAF50)
val StatusOccupied = Color(0xFFF44336)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    background = LightBackground,
    surface = LightSurface,
    error = LightError,
    onBackground = Color.Black,
    onSurface = Color.Black
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    background = DarkBackground,
    surface = DarkSurface,
    error = DarkError,
    onBackground = Color.White,
    onSurface = Color.White
)


@Composable
fun MatchStrartTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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