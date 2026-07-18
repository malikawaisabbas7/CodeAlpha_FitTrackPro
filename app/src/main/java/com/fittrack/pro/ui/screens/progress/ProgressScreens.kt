package com.fittrack.pro.ui.screens.progress

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.fittrack.pro.ui.components.*
import com.fittrack.pro.ui.theme.*
import com.fittrack.pro.viewmodel.FitTrackViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    vm              : FitTrackViewModel,
    onAchievements  : () -> Unit
) {
    val totalWorkouts  by vm.totalWorkouts.collectAsState()
    val totalCalories  by vm.totalCalories.collectAsState()
    val longestWorkout by vm.longestWorkout.collectAsState()
    val avgWorkout     by vm.avgWorkout.collectAsState()
    val weeklyStats    by vm.weeklyStats.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Progress", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onAchievements) {
                        Text("🏅", fontSize = 22.sp)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // ── Summary Rings Row ──────────────────────────────────────────
            SectionHeader(title = "All Time Stats")

            Row(
                modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatSummaryCard(
                    emoji = "🏋️", value = "$totalWorkouts", label = "Workouts",
                    color = DeepBlue, modifier = Modifier.weight(1f)
                )
                StatSummaryCard(
                    emoji = "🔥", value = "${totalCalories}k", label = "Calories",
                    color = OrangeAccent, modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatSummaryCard(
                    emoji = "⏱️", value = "${longestWorkout}m", label = "Longest",
                    color = GradientGreen1, modifier = Modifier.weight(1f)
                )
                StatSummaryCard(
                    emoji = "📊", value = "${avgWorkout.toInt()}m", label = "Avg Time",
                    color = GradientPurple1, modifier = Modifier.weight(1f)
                )
            }

            // ── Weekly Bar Chart (manual) ──────────────────────────────────
            SectionHeader(title = "This Week")

            if (weeklyStats.isNotEmpty()) {
                Card(
                    modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape     = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Steps Per Day", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(16.dp))

                        val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                        val maxSteps = weeklyStats.maxOfOrNull { it.steps }?.coerceAtLeast(1) ?: 1

                        Row(
                            modifier              = Modifier.fillMaxWidth().height(120.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment     = Alignment.Bottom
                        ) {
                            weeklyStats.take(7).forEachIndexed { i, stat ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Bottom
                                ) {
                                    val fraction = stat.steps.toFloat() / maxSteps.toFloat()
                                    Box(
                                        modifier = Modifier
                                            .width(32.dp)
                                            .fillMaxHeight(fraction.coerceAtLeast(0.05f))
                                            .background(
                                                DeepBlue.copy(alpha = 0.7f + fraction * 0.3f),
                                                RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                            )
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(days.getOrElse(i) { "" }, style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(0.6f))
                                }
                            }
                        }
                    }
                }
            }

            // ── Calories Weekly ────────────────────────────────────────────
            SectionHeader(title = "Calories This Week")
            Card(
                modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape     = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    val totalWeekly = weeklyStats.sumOf { it.caloriesBurned }
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("$totalWeekly kcal", style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold, color = OrangeAccent)
                            Text("Total this week", style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(0.6f))
                        }
                        Text("🔥", fontSize = 40.sp)
                    }
                    Spacer(Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { (totalWeekly.toFloat() / 3500f).coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color    = OrangeAccent,
                        trackColor = OrangeAccent.copy(0.2f)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text("Goal: 3,500 kcal/week", style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.5f))
                }
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
fun StatSummaryCard(emoji: String, value: String, label: String, color: Color, modifier: Modifier) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors    = CardDefaults.cardColors(containerColor = color.copy(0.1f))
    ) {
        Column(
            modifier            = Modifier.padding(20.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(emoji, fontSize = 32.sp)
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = color)
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(0.6f))
        }
    }
}

// ── Achievements Screen ───────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(onBack: () -> Unit) {
    val achievements = listOf(
        Triple("💪", "First Workout",  "Log your very first workout"),
        Triple("🔥", "7 Day Streak",   "Workout 7 days in a row"),
        Triple("🏅", "10 Workouts",    "Complete 10 total workouts"),
        Triple("💯", "100 Workouts",   "Complete 100 total workouts"),
        Triple("🚶", "50,000 Steps",   "Walk a total of 50,000 steps"),
        Triple("🎯", "Goal Achiever",  "Hit your daily step goal 5 times"),
        Triple("💧", "Hydration Hero", "Drink 8 glasses for 7 days"),
        Triple("⚡", "HIIT Master",    "Complete 10 HIIT sessions")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Achievements", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
        ) {
            achievements.forEachIndexed { index, (icon, title, desc) ->
                val unlocked = index < 2  // First 2 unlocked as demo
                Card(
                    modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                    shape     = RoundedCornerShape(16.dp),
                    colors    = CardDefaults.cardColors(
                        containerColor = if (unlocked) EmeraldGreen.copy(0.1f) else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier          = Modifier.padding(16.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(if (unlocked) icon else "🔒", fontSize = 36.sp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(title, style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = if (unlocked) EmeraldGreen else MaterialTheme.colorScheme.onSurface.copy(0.5f))
                            Text(desc, style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(0.5f))
                        }
                        if (unlocked) Text("✅", fontSize = 20.sp)
                    }
                }
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}
