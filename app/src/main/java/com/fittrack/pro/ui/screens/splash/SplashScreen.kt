package com.fittrack.pro.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.fittrack.pro.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(if (visible) 1f else 0f, tween(800), label = "alpha")
    val scale by animateFloatAsState(if (visible) 1f else 0.5f,
        spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow), label = "scale")

    LaunchedEffect(Unit) { visible = true; delay(2500); onFinish() }

    Box(modifier = Modifier.fillMaxSize()
        .background(Brush.verticalGradient(listOf(DeepBlue, GradientEnd, Color(0xFF0F172A)))),
        contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.alpha(alpha)) {
            Box(modifier = Modifier.size(100.dp).scale(scale)
                .background(Color.White.copy(0.15f), CircleShape), contentAlignment = Alignment.Center) {
                Text("🏃", fontSize = 52.sp)
            }
            Spacer(Modifier.height(24.dp))
            Text("FitTrack Pro", style = MaterialTheme.typography.displayMedium, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Text("Track.",   color = Color.White.copy(0.8f), style = MaterialTheme.typography.titleLarge)
            Text("Improve.", color = EmeraldGreen, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Achieve.", color = OrangeAccent, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(60.dp))
            CircularProgressIndicator(color = Color.White.copy(0.6f), modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
        }
    }
}
