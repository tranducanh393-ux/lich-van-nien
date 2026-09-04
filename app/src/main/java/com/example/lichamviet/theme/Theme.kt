package com.example.lichamviet.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.lichamviet.data.repository.AppTheme

val M3Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

fun createColorSchemeForTheme(theme: AppTheme, isSystemDark: Boolean): ColorScheme = when (theme) {
    AppTheme.MODERN_BLUE -> if (isSystemDark) {
        darkColorScheme(
            primary = DarkPrimary,
            onPrimary = DarkOnPrimary,
            primaryContainer = DarkPrimaryContainer,
            onPrimaryContainer = DarkOnPrimaryContainer,
            secondary = DarkSecondary,
            onSecondary = DarkOnSecondary,
            secondaryContainer = DarkSecondaryContainer,
            onSecondaryContainer = DarkOnSecondaryContainer,
            background = DarkBg,
            onBackground = DarkOnSurface,
            surface = DarkSurface,
            onSurface = DarkOnSurface,
            surfaceVariant = DarkSurfaceContainer,
            onSurfaceVariant = DarkOnSurfaceVariant,
            outline = DarkOutline,
            outlineVariant = DarkOutlineVariant
        )
    } else {
        lightColorScheme(
            primary = M3BluePrimary,
            onPrimary = M3BlueOnPrimary,
            primaryContainer = M3BluePrimaryContainer,
            onPrimaryContainer = M3BlueOnPrimaryContainer,
            secondary = M3BlueSecondary,
            onSecondary = M3BlueOnSecondary,
            secondaryContainer = M3BlueSecondaryContainer,
            onSecondaryContainer = M3BlueOnSecondaryContainer,
            tertiary = M3BlueTertiary,
            onTertiary = M3BlueOnTertiary,
            tertiaryContainer = M3BlueTertiaryContainer,
            onTertiaryContainer = M3BlueOnTertiaryContainer,
            background = NeutralSurface,
            onBackground = NeutralOnSurface,
            surface = NeutralSurface,
            onSurface = NeutralOnSurface,
            surfaceVariant = NeutralSurfaceContainer,
            onSurfaceVariant = NeutralOnSurfaceVariant,
            outline = NeutralOutline,
            outlineVariant = NeutralOutlineVariant
        )
    }
    AppTheme.JADE_GREEN -> lightColorScheme(
        primary = JadePrimary,
        onPrimary = JadeOnPrimary,
        primaryContainer = JadePrimaryContainer,
        onPrimaryContainer = JadeOnPrimaryContainer,
        secondary = JadeSecondary,
        onSecondary = Color.White,
        secondaryContainer = JadeSecondaryContainer,
        onSecondaryContainer = JadeOnSecondaryContainer,
        tertiary = M3BluePrimary,
        onTertiary = Color.White,
        tertiaryContainer = M3BluePrimaryContainer,
        onTertiaryContainer = M3BlueOnPrimaryContainer,
        background = Color(0xFFFBFDF9),
        onBackground = NeutralOnSurface,
        surface = Color(0xFFFBFDF9),
        onSurface = NeutralOnSurface,
        surfaceVariant = Color(0xFFE8F1EB),
        onSurfaceVariant = NeutralOnSurfaceVariant,
        outline = NeutralOutline,
        outlineVariant = NeutralOutlineVariant
    )
    AppTheme.AMBER_WARM -> lightColorScheme(
        primary = AmberPrimary,
        onPrimary = AmberOnPrimary,
        primaryContainer = AmberPrimaryContainer,
        onPrimaryContainer = AmberOnPrimaryContainer,
        secondary = AmberSecondary,
        onSecondary = Color.White,
        secondaryContainer = AmberSecondaryContainer,
        onSecondaryContainer = AmberOnSecondaryContainer,
        tertiary = JadePrimary,
        onTertiary = Color.White,
        tertiaryContainer = JadePrimaryContainer,
        onTertiaryContainer = JadeOnPrimaryContainer,
        background = Color(0xFFFFF8F4),
        onBackground = NeutralOnSurface,
        surface = Color(0xFFFFF8F4),
        onSurface = NeutralOnSurface,
        surfaceVariant = Color(0xFFF4EDE6),
        onSurfaceVariant = NeutralOnSurfaceVariant,
        outline = NeutralOutline,
        outlineVariant = NeutralOutlineVariant
    )
    AppTheme.DARK_SLEEK -> darkColorScheme(
        primary = DarkPrimary,
        onPrimary = DarkOnPrimary,
        primaryContainer = DarkPrimaryContainer,
        onPrimaryContainer = DarkOnPrimaryContainer,
        secondary = DarkSecondary,
        onSecondary = DarkOnSecondary,
        secondaryContainer = DarkSecondaryContainer,
        onSecondaryContainer = DarkOnSecondaryContainer,
        background = DarkBg,
        onBackground = DarkOnSurface,
        surface = DarkSurface,
        onSurface = DarkOnSurface,
        surfaceVariant = DarkSurfaceContainer,
        onSurfaceVariant = DarkOnSurfaceVariant,
        outline = DarkOutline,
        outlineVariant = DarkOutlineVariant
    )
}

@Composable
fun LichAmVietTheme(
    theme: AppTheme = AppTheme.MODERN_BLUE,
    dynamicColor: Boolean = false,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        theme == AppTheme.DARK_SLEEK -> {
            createColorSchemeForTheme(AppTheme.DARK_SLEEK, true)
        }
        darkTheme -> {
            createColorSchemeForTheme(theme, true)
        }
        else -> {
            createColorSchemeForTheme(theme, false)
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = M3Shapes,
        typography = Typography,
        content = content
    )
}
