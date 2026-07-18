package com.fittrack.pro.ui.screens.activity

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.fittrack.pro.data.database.entity.WorkoutEntity
import com.fittrack.pro.ui.components.*
import com.fittrack.pro.ui.screens.dashboard.WorkoutListItem
import com.fittrack.pro.ui.theme.*
import com.fittrack.pro.viewmodel.FitTrackViewModel
import java.text.SimpleDateFormat
import java.util.*

// ── Exercise Types ────────────────────────────────────────────────────────────
val exerciseTypes = listOf(
    "Walking" to "🚶", "Running" to "🏃", "Cycling" to "🚴",
    "Gym" to "🏋️", "Yoga" to "🧘", "Swimming" to "🏊",
    "HIIT" to "⚡", "Strength Training" to "💪"
)

// ── Activity List Screen ──────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    vm           : FitTrackViewModel,
    onAddClick   : () -> Unit,
    onSearchClick: () -> Unit
) {
    val workouts by vm.allWorkouts.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Activities", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onSearchClick) { Icon(Icons.Default.Search, null) }
                    IconButton(onClick = onAddClick)    { Icon(Icons.Default.Add, null, tint = DeepBlue) }
                }
            )
        }
    ) { padding ->
        if (workouts.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                EmptyState("🏋️", "No Workouts Yet", "Start logging your activities to see them here!")
            }
        } else {
            LazyColumn(
                modifier       = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item {
                    Text("${workouts.size} Total Workouts",
                        style    = MaterialTheme.typography.bodySmall,
                        color    = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp))
                }
                items(workouts, key = { it.id }) { workout ->
                    SwipeToDeleteWorkout(
                        workout  = workout,
                        onDelete = { vm.deleteWorkout(workout) }
                    )
                }
            }
        }
    }
}

// ── Swipe to Delete Wrapper ───────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteWorkout(workout: WorkoutEntity, onDelete: () -> Unit) {
    val dismissState = rememberSwipeToDismissBoxState()
    if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
        LaunchedEffect(Unit) { onDelete() }
    }
    SwipeToDismissBox(
        state            = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier         = Modifier.fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .background(Color(0xFFEF4444), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(Icons.Default.Delete, null, tint = Color.White,
                    modifier = Modifier.padding(end = 20.dp))
            }
        }
    ) { WorkoutListItem(workout) }
}

// ── Log Workout Screen ────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogWorkoutScreen(
    vm    : FitTrackViewModel,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    var name         by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Running") }
    var duration     by remember { mutableStateOf("") }
    var calories     by remember { mutableStateOf("") }
    var distance     by remember { mutableStateOf("") }
    var notes        by remember { mutableStateOf("") }
    val today = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }
    val time  = remember { SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title      = { Text("Log Activity", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.Close, null) } }
            )
        }
    ) { padding ->
        Column(
            modifier             = Modifier.fillMaxSize().padding(padding)
                .verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement  = Arrangement.spacedBy(16.dp)
        ) {
            // Exercise type selector
            Text("Exercise Type", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)

            LazyRowTypeSelector(selectedType) { selectedType = it }

            // Workout name
            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { Text("Workout Name") },
                leadingIcon = { Text(exerciseTypes.find { it.first == selectedType }?.second ?: "🏅", fontSize = 18.sp) },
                singleLine = true, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = duration, onValueChange = { duration = it },
                    label = { Text("Duration (min)") },
                    leadingIcon = { Icon(Icons.Default.Timer, null) },
                    singleLine = true, modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = calories, onValueChange = { calories = it },
                    label = { Text("Calories") },
                    leadingIcon = { Text("🔥", fontSize = 16.sp) },
                    singleLine = true, modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            OutlinedTextField(
                value = distance, onValueChange = { distance = it },
                label = { Text("Distance (km) — optional") },
                leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                singleLine = true, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = notes, onValueChange = { notes = it },
                label = { Text("Notes — optional") },
                leadingIcon = { Icon(Icons.Default.Notes, null) },
                maxLines = 3, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(8.dp))

            FitTrackButton(
                text     = "Save Activity ✓",
                onClick  = {
                    if (name.isNotBlank() && duration.isNotBlank()) {
                        vm.addWorkout(
                            WorkoutEntity(
                                userId        = "",
                                name          = name,
                                type          = selectedType,
                                durationMins  = duration.toIntOrNull() ?: 0,
                                caloriesBurned= calories.toIntOrNull() ?: 0,
                                distanceKm    = distance.toFloatOrNull() ?: 0f,
                                notes         = notes,
                                date          = today,
                                time          = time
                            )
                        )
                        onSave()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled  = name.isNotBlank() && duration.isNotBlank()
            )
        }
    }
}

@Composable
private fun LazyRowTypeSelector(selected: String, onSelect: (String) -> Unit) {
    Row(
        modifier              = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        exerciseTypes.forEach { (type, icon) ->
            val isSelected = type == selected
            FilterChip(
                selected = isSelected,
                onClick  = { onSelect(type) },
                label    = { Text("$icon $type") },
                colors   = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DeepBlue,
                    selectedLabelColor     = Color.White
                )
            )
        }
    }
}

// ── Search Screen ─────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(vm: FitTrackViewModel, onBack: () -> Unit) {
    val query   by vm.searchResults.collectAsState()
    var text    by remember { mutableStateOf("") }

    LaunchedEffect(text) { vm.onSearchQueryChange(text) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value         = text,
                        onValueChange = { text = it },
                        placeholder   = { Text("Search workouts...") },
                        leadingIcon   = { Icon(Icons.Default.Search, null) },
                        trailingIcon  = {
                            if (text.isNotEmpty())
                                IconButton(onClick = { text = "" }) { Icon(Icons.Default.Clear, null) }
                        },
                        singleLine    = true,
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(28.dp)
                    )
                },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier       = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            if (text.isNotBlank()) {
                item {
                    Text("${query.size} results for \"$text\"",
                        style    = MaterialTheme.typography.bodySmall,
                        color    = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                        modifier = Modifier.padding(16.dp))
                }
            }
            items(query) { workout -> WorkoutListItem(workout) }
            if (text.isNotBlank() && query.isEmpty()) {
                item { EmptyState("🔍", "No Results", "Try searching with different keywords") }
            }
        }
    }
}

// ── Water Tracker Screen ──────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterTrackerScreen(vm: FitTrackViewModel, onBack: () -> Unit) {
    val water by vm.todayWater.collectAsState()
    val glasses = water?.glasses ?: 0
    val goal    = water?.goalGlasses ?: 8

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Water Tracker", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GradientPurple1, titleContentColor = Color.White,
                    navigationIconContentColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier            = Modifier.fillMaxSize().padding(padding)
                .background(Brush.verticalGradient(listOf(GradientPurple1.copy(0.1f), MaterialTheme.colorScheme.background))),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))
            CircularProgressRing(
                progress      = glasses.toFloat() / goal.toFloat(),
                value         = "$glasses/$goal",
                label         = "Glasses Today",
                size          = 200.dp,
                strokeWidth   = 16.dp,
                trackColor    = GradientPurple1.copy(0.2f),
                progressColor = GradientPurple1
            )
            Spacer(Modifier.height(40.dp))

            // 8 water glass indicators
            Row(
                modifier              = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                repeat(goal) { i ->
                    Text(if (i < glasses) "💧" else "🫙", fontSize = 32.sp)
                }
            }

            Spacer(Modifier.height(40.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedButton(onClick = { vm.removeWaterGlass() }, enabled = glasses > 0) {
                    Icon(Icons.Default.Remove, null)
                    Spacer(Modifier.width(4.dp))
                    Text("Remove")
                }
                FitTrackButton(
                    text    = "+ Add Glass",
                    onClick = { vm.addWaterGlass() },
                    enabled = glasses < goal,
                    colors  = listOf(GradientPurple1, GradientPurple2),
                    modifier= Modifier.width(160.dp)
                )
            }

            if (glasses >= goal) {
                Spacer(Modifier.height(24.dp))
                Card(modifier = Modifier.padding(horizontal = 20.dp), shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldGreen.copy(0.1f))) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("🎉", fontSize = 28.sp)
                        Spacer(Modifier.width(12.dp))
                        Text("Daily water goal achieved!", fontWeight = FontWeight.SemiBold, color = EmeraldGreen)
                    }
                }
            }
        }
    }
}
