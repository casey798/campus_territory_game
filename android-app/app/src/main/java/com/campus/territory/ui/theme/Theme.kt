package com.campus.territory.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = DeepGreen,
    onPrimary = Color.White,
    secondary = LightGreen,
    background = Sand,
    surface = Color(0xFFF7F1DF)
)

@Composable
fun CampusTerritoryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
