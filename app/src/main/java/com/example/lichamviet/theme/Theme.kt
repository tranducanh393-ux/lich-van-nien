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
            surfaceContainer = Color(0xFF1D2024),
            surfaceContainerHigh = Color(0xFF23262B),
            surfaceContainerHighest = Color(0xFF2B2E34),
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
            surfaceContainer = Color(0xFFF0F4F9),
            surfaceContainerHigh = Color(0xFFE9EEF6),
            surfaceContainerHighest = Color(0xFFE0E5ED),
            outline = NeutralOutline,
            outlineVariant = NeutralOutlineVariant
        )
    }
    AppTheme.JADE_GREEN -> if (isSystemDark) {
        darkColorScheme(
            primary = Color(0xFF7DDC96),
            onPrimary = Color(0xFF00391A),
            primaryContainer = Color(0xFF005328),
            onPrimaryContainer = Color(0xFF99F9B0),
            secondary = Color(0xFFB7CCB8),
            onSecondary = Color(0xFF223526),
            secondaryContainer = Color(0xFF384B3C),
            onSecondaryContainer = Color(0xFFD3E8D3),
            tertiary = Color(0xFFA5CDD6),
            onTertiary = Color(0xFF07353C),
            tertiaryContainer = Color(0xFF224B53),
            onTertiaryContainer = Color(0xFFC1E9F2),
            background = Color(0xFF0E1511),
            onBackground = Color(0xFFDEE4DD),
            surface = Color(0xFF131B16),
            onSurface = Color(0xFFDEE4DD),
            surfaceVariant = Color(0xFF212C24),
            onSurfaceVariant = Color(0xFFC0C9C0),
            surfaceContainer = Color(0xFF1C251F),
            surfaceContainerHigh = Color(0xFF242F28),
            surfaceContainerHighest = Color(0xFF2C3830),
            outline = Color(0xFF8A938B),
            outlineVariant = Color(0xFF404941)
        )
    } else {
        lightColorScheme(
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
            onBackground = Color(0xFF191C19),
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF191C19),
            surfaceVariant = Color(0xFFE8F1EB),
            onSurfaceVariant = Color(0xFF404941),
            surfaceContainer = Color(0xFFEFF5F0),
            surfaceContainerHigh = Color(0xFFE6EFE8),
            surfaceContainerHighest = Color(0xFFDDE7DF),
            outline = NeutralOutline,
            outlineVariant = NeutralOutlineVariant
        )
    }
    AppTheme.AMBER_WARM -> if (isSystemDark) {
        darkColorScheme(
            primary = Color(0xFFFFB77D),
            onPrimary = Color(0xFF4D2600),
            primaryContainer = Color(0xFF6E3900),
            onPrimaryContainer = Color(0xFFFFDCC1),
            secondary = Color(0xFFE5BFA8),
            onSecondary = Color(0xFF422B1B),
            secondaryContainer = Color(0xFF5B4130),
            onSecondaryContainer = Color(0xFFFFDCC1),
            tertiary = Color(0xFFD6C88A),
            onTertiary = Color(0xFF383000),
            tertiaryContainer = Color(0xFF504600),
            onTertiaryContainer = Color(0xFFF3E4A3),
            background = Color(0xFF17120D),
            onBackground = Color(0xFFEFE0D7),
            surface = Color(0xFF1F1812),
            onSurface = Color(0xFFEFE0D7),
            surfaceVariant = Color(0xFF2D231C),
            onSurfaceVariant = Color(0xFFD6C3B6),
            surfaceContainer = Color(0xFF271E18),
            surfaceContainerHigh = Color(0xFF302720),
            surfaceContainerHighest = Color(0xFF3B3029),
            outline = Color(0xFF9E8E83),
            outlineVariant = Color(0xFF51443B)
        )
    } else {
        lightColorScheme(
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
            onBackground = Color(0xFF211A14),
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF211A14),
            surfaceVariant = Color(0xFFF4EDE6),
            onSurfaceVariant = Color(0xFF51443B),
            surfaceContainer = Color(0xFFFBF2EB),
            surfaceContainerHigh = Color(0xFFF5EAE1),
            surfaceContainerHighest = Color(0xFFEFE2D7),
            outline = NeutralOutline,
            outlineVariant = NeutralOutlineVariant
        )
    }
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
        surfaceContainer = Color(0xFF1D2024),
        surfaceContainerHigh = Color(0xFF23262B),
        surfaceContainerHighest = Color(0xFF2B2E34),
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
