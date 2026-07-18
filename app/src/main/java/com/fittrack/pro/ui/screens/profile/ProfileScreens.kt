package com.fittrack.pro.ui.screens.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.fittrack.pro.ui.components.FitTrackButton
import com.fittrack.pro.ui.theme.*
import com.fittrack.pro.viewmodel.FitTrackViewModel

// ── Profile Screen ────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    vm        : FitTrackViewModel,
    onEdit    : () -> Unit,
    onBMI     : () -> Unit,
    onSettings: () -> Unit,
    onLogout  : () -> Unit,
    // New parameters to handle the previously non-functional buttons
    onHelp    : () -> Unit,
    onPrivacy : () -> Unit,
    onAbout   : () -> Unit
) {
    val totalWorkouts by vm.totalWorkouts.collectAsState()
    val totalCalories by vm.totalCalories.collectAsState()
    // Observe the real username from ViewModel
    val userName by vm.userName.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(DeepBlue, GradientEnd)))
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(Color.White.copy(0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) { Text("🏃", fontSize = 44.sp) }
                Spacer(Modifier.height(12.dp))
                // Display the dynamic username
                Text(userName, style = MaterialTheme.typography.titleLarge,
                    color = Color.White, fontWeight = FontWeight.Bold)
                Text("Fitness Enthusiast", style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(0.7f))
                Spacer(Modifier.height(16.dp))
                OutlinedButton(onClick = onEdit,
                    border = BorderStroke(1.dp, Color.White.copy(0.6f))) {
                    Icon(Icons.Default.Edit, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Edit Profile", color = Color.White)
                }
            }
        }

        // Stats row
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileStatCard("🏋️", "$totalWorkouts", "Workouts", DeepBlue, Modifier.weight(1f))
            ProfileStatCard("🔥", "$totalCalories", "Calories", OrangeAccent, Modifier.weight(1f))
            ProfileStatCard("🏅", "2", "Badges", EmeraldGreen, Modifier.weight(1f))
        }

        // Menu items - Now fully linked to actions
        val menuItems = listOf(
            Triple("⚖️", "BMI Calculator", onBMI),
            Triple("⚙️", "Settings",       onSettings),
            Triple("❓", "Help & Support",  onHelp),
            Triple("📄", "Privacy Policy",  onPrivacy),
            Triple("ℹ️", "About FitTrack",  onAbout)
        )

        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(2.dp)) {
            Column {
                menuItems.forEachIndexed { i, (icon, label, action) ->
                    ListItem(
                        headlineContent = { Text(label, style = MaterialTheme.typography.bodyLarge) },
                        leadingContent  = { Text(icon, fontSize = 22.sp) },
                        trailingContent = { Icon(Icons.Default.ChevronRight, null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(0.4f)) },
                        modifier        = Modifier.clickable(onClick = action)
                    )
                    if (i < menuItems.size - 1) HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }

        // Logout
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFEF4444).copy(0.1f))) {
            ListItem(
                headlineContent = { Text("Logout", color = Color(0xFFEF4444), fontWeight = FontWeight.SemiBold) },
                leadingContent  = { Icon(Icons.Default.Logout, null, tint = Color(0xFFEF4444)) },
                modifier        = Modifier.clickable(onClick = onLogout)
            )
        }
        Spacer(Modifier.height(80.dp))
    }
}

@Composable
fun ProfileStatCard(emoji: String, value: String, label: String, color: Color, modifier: Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(0.1f))) {
        Column(modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(emoji, fontSize = 24.sp)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.6f))
        }
    }
}

// ── Edit Profile Screen ───────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(vm: FitTrackViewModel, onBack: () -> Unit) {
    // Initialize with current values from ViewModel
    val currentName by vm.userName.collectAsState()

    var name   by remember { mutableStateOf(currentName) }
    var height by remember { mutableStateOf("170") }
    var weight by remember { mutableStateOf("70") }
    var age    by remember { mutableStateOf("25") }
    var gender by remember { mutableStateOf("Male") }
    val goals  = listOf("Stay Fit", "Lose Weight", "Build Muscle", "Run a Marathon", "Improve Endurance")
    var goal   by remember { mutableStateOf("Stay Fit") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
            .padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {

            OutlinedTextField(value = name, onValueChange = { name = it },
                label = { Text("Full Name") }, leadingIcon = { Icon(Icons.Default.Person, null) },
                singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = height, onValueChange = { height = it },
                    label = { Text("Height (cm)") }, singleLine = true,
                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
                OutlinedTextField(value = weight, onValueChange = { weight = it },
                    label = { Text("Weight (kg)") }, singleLine = true,
                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = age, onValueChange = { age = it },
                    label = { Text("Age") }, singleLine = true,
                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))

                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it },
                    modifier = Modifier.weight(1f)) {
                    OutlinedTextField(value = gender, onValueChange = {},
                        label = { Text("Gender") }, readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        listOf("Male", "Female", "Other").forEach {
                            DropdownMenuItem(text = { Text(it) }, onClick = { gender = it; expanded = false })
                        }
                    }
                }
            }

            Text("Fitness Goal", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                goals.forEach { g ->
                    FilterChip(selected = goal == g, onClick = { goal = g }, label = { Text(g) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DeepBlue, selectedLabelColor = Color.White))
                }
            }

            FitTrackButton(
                text = "Save Changes",
                onClick = {
                    // SAVE THE NAME TO VIEWMODEL PERMANENTLY
                    vm.updateUserName(name)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ── BMI Calculator Screen (No changes needed, already functional) ──────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMICalculatorScreen(vm: FitTrackViewModel, onBack: () -> Unit) {
    var height  by remember { mutableStateOf("") }
    var weight  by remember { mutableStateOf("") }
    var bmi     by remember { mutableStateOf<Float?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BMI Calculator", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {

            Text("⚖️", fontSize = 64.sp)
            Text("Calculate Your BMI", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Body Mass Index measures body fat based on height and weight",
                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(0.6f))

            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = height, onValueChange = { height = it },
                    label = { Text("Height (cm)") }, leadingIcon = { Text("📏", fontSize = 18.sp) },
                    singleLine = true, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
                OutlinedTextField(value = weight, onValueChange = { weight = it },
                    label = { Text("Weight (kg)") }, leadingIcon = { Text("⚖️", fontSize = 18.sp) },
                    singleLine = true, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
            }

            FitTrackButton(
                text    = "Calculate BMI",
                onClick = {
                    val h = height.toFloatOrNull()
                    val w = weight.toFloatOrNull()
                    if (h != null && w != null) bmi = vm.calculateBMI(h, w)
                },
                modifier= Modifier.fillMaxWidth(),
                enabled = height.isNotBlank() && weight.isNotBlank()
            )

            bmi?.let { b ->
                val category = vm.getBMICategory(b)
                val (emoji, color) = when (category) {
                    "Underweight" -> "😟" to Color(0xFF3B82F6)
                    "Normal"      -> "😊" to EmeraldGreen
                    "Overweight"  -> "😐" to OrangeAccent
                    else          -> "😟" to Color(0xFFEF4444)
                }
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = color.copy(0.1f))) {
                    Column(modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(emoji, fontSize = 48.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("%.1f".format(b), style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold, color = color)
                        Text(category, style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold, color = color)
                    }
                }

                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("BMI Scale", fontWeight = FontWeight.SemiBold)
                        listOf(
                            Triple("< 18.5", "Underweight", Color(0xFF3B82F6)),
                            Triple("18.5 – 24.9", "Normal", EmeraldGreen),
                            Triple("25.0 – 29.9", "Overweight", OrangeAccent),
                            Triple("> 30.0", "Obese", Color(0xFFEF4444))
                        ).forEach { (range, cat, c) ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically) {
                                Text(range, style = MaterialTheme.typography.bodySmall)
                                Text(cat, color = c, fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Settings Screen (Now Functional) ──────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(vm: FitTrackViewModel, onBack: () -> Unit, onLogout: () -> Unit) {
    // Linked to ViewModel for real functionality
    val isDarkMode by vm.isDarkMode.collectAsState()
    var notifications  by remember { mutableStateOf(true) }
    var metric         by remember { mutableStateOf(true) }
    var waterReminder  by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())) {
            SettingsSection(title = "Appearance") {
                // Now toggle actually changes the app's theme
                SettingsToggle("🌙", "Dark Mode", "Use dark theme throughout the app", isDarkMode) {
                    vm.toggleDarkMode(it)
                }
            }
            SettingsSection(title = "Notifications") {
                SettingsToggle("🔔", "Workout Reminders", "Get daily workout reminders", notifications) { notifications = it }
                SettingsToggle("💧", "Water Reminders", "Reminders to drink water", waterReminder) { waterReminder = it }
            }
            SettingsSection(title = "Units") {
                SettingsToggle("📏", "Metric Units", "Use km, kg, cm", metric) { metric = it }
            }
            SettingsSection(title = "Account") {
                ListItem(
                    headlineContent = { Text("Logout", color = Color(0xFFEF4444)) },
                    leadingContent  = { Icon(Icons.Default.Logout, null, tint = Color(0xFFEF4444)) },
                    modifier        = Modifier.clickable(onClick = onLogout)
                )
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column {
        Text(title, style = MaterialTheme.typography.labelLarge, color = DeepBlue,
            fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
            shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(1.dp)) {
            content()
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
fun SettingsToggle(icon: String, title: String, subtitle: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    ListItem(
        headlineContent  = { Text(title) },
        supportingContent= { Text(subtitle, style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(0.6f)) },
        leadingContent   = { Text(icon, fontSize = 22.sp) },
        trailingContent  = { Switch(checked = checked, onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = DeepBlue)) }
    )
}