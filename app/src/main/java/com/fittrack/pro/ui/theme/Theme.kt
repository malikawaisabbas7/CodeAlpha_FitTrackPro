package com.fittrack.pro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fittrack.pro.R


// ── Colors ────────────────────────────────────────────────────────────────────
val DeepBlue        = Color(0xFF2563EB)
val EmeraldGreen    = Color(0xFF22C55E)
val OrangeAccent    = Color(0xFFF97316)
val LightBg         = Color(0xFFF8FAFC)
val DarkBg          = Color(0xFF0F172A)
val CardLight       = Color(0xFFFFFFFF)
val CardDark        = Color(0xFF1E293B)
val TextPrimary     = Color(0xFF1E293B)
val TextSecondary   = Color(0xFF64748B)
val TextOnDark      = Color(0xFFF1F5F9)
val GradientEnd     = Color(0xFF7C3AED)
val GradientGreen1  = Color(0xFF22C55E)
val GradientGreen2  = Color(0xFF16A34A)
val GradientOrange1 = Color(0xFFF97316)
val GradientOrange2 = Color(0xFFEA580C)
val GradientPurple1 = Color(0xFF8B5CF6)
val GradientPurple2 = Color(0xFF7C3AED)

private val LightColorScheme = lightColorScheme(
    primary          = DeepBlue,
    onPrimary        = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    secondary        = EmeraldGreen,
    onSecondary      = Color.White,
    tertiary         = OrangeAccent,
    background       = LightBg,
    surface          = CardLight,
    onBackground     = TextPrimary,
    onSurface        = TextPrimary,
    surfaceVariant   = Color(0xFFF1F5F9),
    outline          = Color(0xFFCBD5E1)
)

private val DarkColorScheme = darkColorScheme(
    primary          = Color(0xFF60A5FA),
    onPrimary        = Color(0xFF1E3A5F),
    primaryContainer = Color(0xFF1D4ED8),
    secondary        = EmeraldGreen,
    onSecondary      = Color.White,
    tertiary         = OrangeAccent,
    background       = DarkBg,
    surface          = CardDark,
    onBackground     = TextOnDark,
    onSurface        = TextOnDark,
    surfaceVariant   = Color(0xFF1E293B),
    outline          = Color(0xFF334155)
)

// The fix: Using named parameters 'resId' and 'weight' to solve compiler ambiguity
val PoppinsFamily = FontFamily(
    Font(resId = R.font.poppins_regular,  weight = FontWeight.Normal),
    Font(resId = R.font.poppins_medium,   weight = FontWeight.Medium),
    Font(resId = R.font.poppins_semibold, weight = FontWeight.SemiBold),
    Font(resId = R.font.poppins_bold,     weight = FontWeight.Bold),
)

val FitTrackTypography = Typography(
    displayLarge   = TextStyle(fontFamily = PoppinsFamily, fontWeight = FontWeight.Bold,     fontSize = 57.sp),
    headlineLarge  = TextStyle(fontFamily = PoppinsFamily, fontWeight = FontWeight.Bold,     fontSize = 32.sp),
    headlineMedium = TextStyle(fontFamily = PoppinsFamily, fontWeight = FontWeight.Bold,     fontSize = 28.sp),
    headlineSmall  = TextStyle(fontFamily = PoppinsFamily, fontWeight = FontWeight.SemiBold, fontSize = 24.sp),
    titleLarge     = TextStyle(fontFamily = PoppinsFamily, fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
    titleMedium    = TextStyle(fontFamily = PoppinsFamily, fontWeight = FontWeight.Medium,   fontSize = 16.sp),
    titleSmall     = TextStyle(fontFamily = PoppinsFamily, fontWeight = FontWeight.Medium,   fontSize = 14.sp),
    bodyLarge      = TextStyle(fontFamily = PoppinsFamily, fontWeight = FontWeight.Normal,   fontSize = 16.sp),
    bodyMedium     = TextStyle(fontFamily = PoppinsFamily, fontWeight = FontWeight.Normal,   fontSize = 14.sp),
    bodySmall      = TextStyle(fontFamily = PoppinsFamily, fontWeight = FontWeight.Normal,   fontSize = 12.sp),
    labelLarge     = TextStyle(fontFamily = PoppinsFamily, fontWeight = FontWeight.Medium,   fontSize = 14.sp),
    labelSmall     = TextStyle(fontFamily = PoppinsFamily, fontWeight = FontWeight.Medium,   fontSize = 11.sp),
)

@Composable
fun FitTrackProTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content  : @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = FitTrackTypography,
        content = content
    )
}