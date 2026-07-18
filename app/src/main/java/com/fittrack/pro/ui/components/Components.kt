package com.fittrack.pro.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.fittrack.pro.ui.theme.*

// ── Gradient Card ─────────────────────────────────────────────────────────────
@Composable
fun GradientCard(
    modifier     : Modifier = Modifier,
    colors       : List<Color>,
    cornerRadius : Dp = 20.dp,
    content      : @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(Brush.linearGradient(colors))
            .padding(20.dp),
        content = content
    )
}

// ── Metric Card ───────────────────────────────────────────────────────────────
@Composable
fun MetricCard(
    title    : String,
    value    : String,
    unit     : String,
    icon     : String,
    colors   : List<Color>,
    modifier : Modifier = Modifier,
    progress : Float    = 0f
) {
    GradientCard(modifier = modifier.height(140.dp), colors = colors) {
        Column(
            modifier            = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(icon, fontSize = 28.sp)
                if (progress > 0f) SmallProgressRing(progress = progress, color = Color.White.copy(alpha = 0.9f))
            }
            Column {
                Text(value, style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Text(unit,  style = MaterialTheme.typography.bodySmall,      color = Color.White.copy(alpha = 0.8f))
                Text(title, style = MaterialTheme.typography.labelSmall,     color = Color.White.copy(alpha = 0.7f))
            }
        }
    }
}

// ── Large Circular Progress Ring ──────────────────────────────────────────────
@Composable
fun CircularProgressRing(
    progress      : Float,
    value         : String,
    label         : String,
    size          : Dp    = 180.dp,
    strokeWidth   : Dp    = 14.dp,
    trackColor    : Color = Color.White.copy(alpha = 0.2f),
    progressColor : Color = Color.White
) {
    val animated by animateFloatAsState(
        targetValue   = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label         = "ring"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier         = Modifier.size(size)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val diameter    = this.size.minDimension
            val strokePx    = strokeWidth.toPx()
            val radius      = diameter / 2f - strokePx / 2f
            val centerX     = this.size.width  / 2f
            val centerY     = this.size.height / 2f
            val topLeftX    = centerX - radius
            val topLeftY    = centerY - radius
            val arcSize     = Size(radius * 2f, radius * 2f)
            val strokeStyle = Stroke(width = strokePx, cap = StrokeCap.Round)

            // Background track arc
            drawArc(
                color      = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter  = false,
                style      = strokeStyle,
                topLeft    = Offset(topLeftX, topLeftY),
                size       = arcSize
            )

            // Animated progress arc
            drawArc(
                color      = progressColor,
                startAngle = -90f,
                sweepAngle = animated * 360f,
                useCenter  = false,
                style      = strokeStyle,
                topLeft    = Offset(topLeftX, topLeftY),
                size       = arcSize
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text       = value,
                style      = MaterialTheme.typography.headlineMedium,
                color      = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text      = label,
                style     = MaterialTheme.typography.bodySmall,
                color     = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

// ── Small Progress Ring (inside MetricCard) ───────────────────────────────────
@Composable
fun SmallProgressRing(
    progress : Float,
    color    : Color,
    size     : Dp = 36.dp,
    stroke   : Dp = 4.dp
) {
    val animated by animateFloatAsState(
        targetValue   = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 800),
        label         = "sr"
    )

    Canvas(modifier = Modifier.size(size)) {
        val strokePx    = stroke.toPx()
        val radius      = this.size.minDimension / 2f - strokePx / 2f
        val centerX     = this.size.width  / 2f
        val centerY     = this.size.height / 2f
        val topLeftX    = centerX - radius
        val topLeftY    = centerY - radius
        val arcSize     = Size(radius * 2f, radius * 2f)
        val strokeStyle = Stroke(width = strokePx, cap = StrokeCap.Round)

        // Background track
        drawArc(
            color      = Color.White.copy(alpha = 0.3f),
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter  = false,
            style      = strokeStyle,
            topLeft    = Offset(topLeftX, topLeftY),
            size       = arcSize
        )

        // Progress fill
        drawArc(
            color      = color,
            startAngle = -90f,
            sweepAngle = animated * 360f,
            useCenter  = false,
            style      = strokeStyle,
            topLeft    = Offset(topLeftX, topLeftY),
            size       = arcSize
        )
    }
}

// ── Primary Gradient Button ───────────────────────────────────────────────────
@Composable
fun FitTrackButton(
    text      : String,
    onClick   : () -> Unit,
    modifier  : Modifier    = Modifier,
    enabled   : Boolean     = true,
    isLoading : Boolean     = false,
    colors    : List<Color> = listOf(DeepBlue, GradientEnd)
) {
    Box(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                brush = if (enabled) Brush.horizontalGradient(colors)
                else Brush.horizontalGradient(listOf(Color.Gray, Color.DarkGray))
            )
            .clickable(enabled = enabled && !isLoading, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color        = Color.White,
                modifier     = Modifier.size(24.dp),
                strokeWidth  = 2.dp
            )
        } else {
            Text(
                text       = text,
                color      = Color.White,
                style      = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ── Section Header ────────────────────────────────────────────────────────────
@Composable
fun SectionHeader(
    title      : String,
    actionText : String  = "",
    onAction   : () -> Unit = {}
) {
    Row(
        modifier              = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        if (actionText.isNotEmpty()) {
            Text(
                text     = actionText,
                style    = MaterialTheme.typography.bodySmall,
                color    = DeepBlue,
                modifier = Modifier.clickable(onClick = onAction)
            )
        }
    }
}

// ── Empty State ───────────────────────────────────────────────────────────────
@Composable
fun EmptyState(emoji: String, title: String, message: String) {
    Column(
        modifier            = Modifier.fillMaxWidth().padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(emoji, fontSize = 64.sp)
        Spacer(Modifier.height(16.dp))
        Text(
            text       = title,
            style      = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign  = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text      = message,
            style     = MaterialTheme.typography.bodyMedium,
            color     = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

// ── Workout Icon Helper ───────────────────────────────────────────────────────
fun workoutIcon(type: String): String = when (type.lowercase()) {
    "running"           -> "🏃"
    "walking"           -> "🚶"
    "cycling"           -> "🚴"
    "gym"               -> "🏋️"
    "yoga"              -> "🧘"
    "swimming"          -> "🏊"
    "hiit"              -> "⚡"
    "strength training" -> "💪"
    else                -> "🏅"
}