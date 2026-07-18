package com.fittrack.pro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.fittrack.pro.ui.navigation.FitTrackNavHost
import com.fittrack.pro.ui.navigation.Routes
import com.fittrack.pro.ui.theme.*
import com.fittrack.pro.viewmodel.AuthViewModel
import com.fittrack.pro.viewmodel.FitTrackViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Obtain the ViewModel instance
            val fitTrackVM: FitTrackViewModel = viewModel()
            val authVM: AuthViewModel = viewModel()

            // OBSERVE DARK MODE: This is the secret to making the toggle work!
            val isDarkMode by fitTrackVM.isDarkMode.collectAsState()

            // Pass the observed state into your Theme
            FitTrackProTheme(darkTheme = isDarkMode) {
                FitTrackApp(fitTrackVM, authVM)
            }
        }
    }
}

data class BottomNavItem(val label: String, val route: String, val icon: ImageVector)

val bottomNavItems = listOf(
    BottomNavItem("Home",     Routes.DASHBOARD, Icons.Default.Home),
    BottomNavItem("Activity", Routes.ACTIVITY,  Icons.Default.FitnessCenter),
    BottomNavItem("Progress", Routes.PROGRESS,  Icons.Default.BarChart),
    BottomNavItem("Profile",  Routes.PROFILE,   Icons.Default.Person)
)

@Composable
fun FitTrackApp(fitTrackVM: FitTrackViewModel, authVM: AuthViewModel) {
    val navController = rememberNavController()
    val backStack     by navController.currentBackStackEntryAsState()
    val currentRoute  = backStack?.destination?.route
    val mainRoutes    = setOf(Routes.DASHBOARD, Routes.ACTIVITY, Routes.PROGRESS, Routes.PROFILE)
    val showBottomBar = currentRoute in mainRoutes

    Scaffold(
        modifier  = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute == item.route
                        NavigationBarItem(
                            selected = selected,
                            onClick  = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon  = { Icon(item.icon, item.label) },
                            label = {
                                Text(
                                    item.label,
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor   = DeepBlue,
                                selectedTextColor   = DeepBlue,
                                indicatorColor      = DeepBlue.copy(0.1f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // FIXED: Using innerPadding ensures the screen content is not hidden by the bottom bar
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            FitTrackNavHost(
                navController = navController,
                fitTrackVM = fitTrackVM,
                authVM = authVM,
                startDestination = Routes.SPLASH
            )
        }
    }
}