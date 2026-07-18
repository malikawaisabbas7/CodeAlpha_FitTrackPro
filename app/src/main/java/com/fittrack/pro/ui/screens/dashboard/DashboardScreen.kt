package com.fittrack.pro.ui.screens.dashboard

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.fittrack.pro.ui.components.*
import com.fittrack.pro.ui.theme.*
import com.fittrack.pro.viewmodel.FitTrackViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    vm                : FitTrackViewModel,
    onLogWorkout      : () -> Unit,
    onSeeAllActivities: () -> Unit,
    onWaterTap        : () -> Unit
) {
    val todayStats    by vm.todayStats.collectAsState()
    val todayWorkouts by vm.todayWorkouts.collectAsState()
    val todayWater    by vm.todayWater.collectAsState()

    val hour   = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when {
        hour < 12 -> "Good Morning"
        hour < 17 -> "Good Afternoon"
        else      -> "Good Evening"
    }

    val steps      = todayStats?.steps          ?: 0
    val calories   = todayStats?.caloriesBurned ?: 0
    val minutes    = todayStats?.workoutMinutes ?: 0
    val water      = todayWater?.glasses        ?: 0
    val stepGoal   = todayStats?.stepGoal       ?: 10000

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick           = onLogWorkout,
                containerColor    = DeepBlue,
                contentColor      = Color.White,
                icon              = { Icon(Icons.Default.Add, null) },
                text              = { Text("Log Activity", fontWeight = FontWeight.SemiBold) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            // ── Header ────────────────────────────────────────────────────────
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(DeepBlue, GradientEnd)))
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("$greeting,", color = Color.White.copy(0.8f),
                                style = MaterialTheme.typography.bodyLarge)
                            Text("Champ 👋", color = Color.White,
                                style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            modifier         = Modifier.size(44.dp)
                                .background(Color.White.copy(0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) { Text("🏃", fontSize = 22.sp) }
                    }

                    Spacer(Modifier.height(24.dp))

                    // Circular ring for steps
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        CircularProgressRing(
                            progress  = (steps.toFloat() / stepGoal).coerceIn(0f, 1f),
                            value     = steps.toString(),
                            label     = "Steps\nGoal: $stepGoal",
                            size      = 160.dp,
                            strokeWidth = 12.dp
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            MiniStatBadge("🔥", "$calories", "kcal")
                            MiniStatBadge("⏱️", "$minutes", "mins")
                            MiniStatBadge("💧", "$water/8", "glasses")
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Metric Cards Row ──────────────────────────────────────────────
            SectionHeader(title = "Today's Stats")

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    MetricCard(
                        title = "Steps", value = steps.toString(), unit = "/ $stepGoal goal",
                        icon = "🚶", progress = steps.toFloat() / stepGoal,
                        colors = listOf(DeepBlue, GradientEnd),
                        modifier = Modifier.width(160.dp)
                    )
                }
                item {
                    MetricCard(
                        title = "Calories", value = "$calories", unit = "kcal burned",
                        icon = "🔥", progress = calories.toFloat() / 500f,
                        colors = listOf(GradientOrange1, GradientOrange2),
                        modifier = Modifier.width(160.dp)
                    )
                }
                item {
                    MetricCard(
                        title = "Workout", value = "$minutes", unit = "minutes",
                        icon = "⏱️", progress = minutes.toFloat() / 60f,
                        colors = listOf(GradientGreen1, GradientGreen2),
                        modifier = Modifier.width(160.dp)
                    )
                }
                item {
                    MetricCard(
                        title = "Water", value = "$water/8", unit = "glasses",
                        icon = "💧", progress = water.toFloat() / 8f,
                        colors = listOf(GradientPurple1, GradientPurple2),
                        modifier = Modifier.width(160.dp).clickable(onClick = onWaterTap)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Motivational Quote ────────────────────────────────────────────
            val quotes = listOf(
                "The only bad workout is the one that didn't happen.",
                "Push yourself, because no one else is going to do it for you.",
                "Your body can stand almost anything. It's your mind you have to convince.",
                "Fitness is not about being better than someone else. It's about being better than you used to be."
            )
            val quote = remember { quotes.random() }

            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape    = RoundedCornerShape(20.dp),
                colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                    Text("💬", fontSize = 24.sp)
                    Spacer(Modifier.width(12.dp))
                    Text(text = "\"$quote\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.weight(1f))
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Recent Activities ─────────────────────────────────────────────
            SectionHeader(
                title      = "Recent Activities",
                actionText = "See All",
                onAction   = onSeeAllActivities
            )

            if (todayWorkouts.isEmpty()) {
                EmptyState(
                    emoji   = "🏋️",
                    title   = "No Activities Yet",
                    message = "Tap the button below to log your first workout today!"
                )
            } else {
                todayWorkouts.take(3).forEach { workout ->
                    WorkoutListItem(workout = workout)
                }
            }

            Spacer(Modifier.height(100.dp))
        }
    }
}

@Composable
private fun MiniStatBadge(icon: String, value: String, unit: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier.size(36.dp)
            .background(Color.White.copy(0.15f), CircleShape),
            contentAlignment = Alignment.Center) {
            Text(icon, fontSize = 16.sp)
        }
        Column {
            Text(value, color = Color.White, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(unit,  color = Color.White.copy(0.7f), style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun WorkoutListItem(workout: com.fittrack.pro.data.database.entity.WorkoutEntity) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation= CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier          = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.size(48.dp)
                    .background(DeepBlue.copy(0.1f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center) {
                    Text(workoutIcon(workout.type), fontSize = 24.sp)
                }
                Column {
                    Text(workout.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Text("${workout.type} • ${workout.durationMins} min",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.6f))
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${workout.caloriesBurned} kcal",
                    style = MaterialTheme.typography.labelLarge, color = OrangeAccent, fontWeight = FontWeight.Bold)
                Text(workout.time, style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.5f))
            }
        }
    }
}