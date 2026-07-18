package com.fittrack.pro.ui.navigation

import android.widget.Toast // Added for "Coming Soon" messages
import androidx.compose.animation.core.tween
import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext // Needed for Toasts
import androidx.navigation.*
import androidx.navigation.compose.*
import com.fittrack.pro.ui.screens.auth.*
import com.fittrack.pro.ui.screens.dashboard.DashboardScreen
import com.fittrack.pro.ui.screens.activity.*
import com.fittrack.pro.ui.screens.progress.*
import com.fittrack.pro.ui.screens.profile.*
import com.fittrack.pro.ui.screens.splash.SplashScreen
import com.fittrack.pro.viewmodel.AuthViewModel
import com.fittrack.pro.viewmodel.FitTrackViewModel

object Routes {
    const val SPLASH       = "splash"
    const val LOGIN        = "login"
    const val REGISTER     = "register"
    const val FORGOT_PW    = "forgot_password"
    const val DASHBOARD    = "dashboard"
    const val ACTIVITY     = "activity"
    const val LOG_WORKOUT  = "log_workout"
    const val PROGRESS     = "progress"
    const val PROFILE      = "profile"
    const val EDIT_PROFILE = "edit_profile"
    const val BMI_CALC     = "bmi_calculator"
    const val WATER        = "water_tracker"
    const val SETTINGS     = "settings"
    const val SEARCH       = "search"
    const val ACHIEVEMENTS = "achievements"
}

@Composable
fun FitTrackNavHost(navController: NavHostController, fitTrackVM: FitTrackViewModel,
                    authVM: AuthViewModel, startDestination: String) {

    val context = LocalContext.current // Used to show messages for unlinked buttons

    NavHost(navController = navController, startDestination = startDestination,
        enterTransition = { fadeIn(tween(300)) + slideInHorizontally { it / 4 } },
        exitTransition  = { fadeOut(tween(200)) },
        popEnterTransition  = { fadeIn(tween(300)) + slideInHorizontally { -it / 4 } },
        popExitTransition   = { fadeOut(tween(200)) + slideOutHorizontally { it / 4 } }
    ) {
        composable(Routes.SPLASH) {
            SplashScreen {
                navController.navigate(if (authVM.isLoggedIn) Routes.DASHBOARD else Routes.LOGIN) {
                    popUpTo(Routes.SPLASH) { inclusive = true } }
            }
        }
        composable(Routes.LOGIN) {
            LoginScreen(authVM,
                onLoginSuccess  = { navController.navigate(Routes.DASHBOARD) { popUpTo(Routes.LOGIN) { inclusive = true } } },
                onRegisterClick = { navController.navigate(Routes.REGISTER) },
                onForgotClick   = { navController.navigate(Routes.FORGOT_PW) })
        }
        composable(Routes.REGISTER) {
            RegisterScreen(authVM,
                onRegisterSuccess = { navController.navigate(Routes.DASHBOARD) { popUpTo(Routes.LOGIN) { inclusive = true } } },
                onLoginClick      = { navController.popBackStack() })
        }
        composable(Routes.FORGOT_PW) {
            ForgotPasswordScreen(authVM, onBack = { navController.popBackStack() }) }

        composable(Routes.DASHBOARD) {
            DashboardScreen(fitTrackVM,
                onLogWorkout       = { navController.navigate(Routes.LOG_WORKOUT) },
                onSeeAllActivities = { navController.navigate(Routes.ACTIVITY) },
                onWaterTap         = { navController.navigate(Routes.WATER) })
        }

        composable(Routes.ACTIVITY) {
            ActivityScreen(fitTrackVM,
                onAddClick    = { navController.navigate(Routes.LOG_WORKOUT) },
                onSearchClick = { navController.navigate(Routes.SEARCH) })
        }

        composable(Routes.LOG_WORKOUT) {
            LogWorkoutScreen(fitTrackVM, onSave = { navController.popBackStack() }, onBack = { navController.popBackStack() }) }

        composable(Routes.PROGRESS) {
            ProgressScreen(fitTrackVM, onAchievements = { navController.navigate(Routes.ACHIEVEMENTS) }) }

        // --- FIXED PROFILE ROUTE ---
        composable(Routes.PROFILE) {
            ProfileScreen(
                vm         = fitTrackVM,
                onEdit     = { navController.navigate(Routes.EDIT_PROFILE) },
                onBMI      = { navController.navigate(Routes.BMI_CALC) },
                onSettings = { navController.navigate(Routes.SETTINGS) },
                onLogout   = {
                    fitTrackVM.logout()
                    navController.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } }
                },
                // RESOLVED ERRORS: Added the three missing parameters
                onHelp     = { Toast.makeText(context, "Help center coming soon!", Toast.LENGTH_SHORT).show() },
                onPrivacy  = { Toast.makeText(context, "Privacy Policy coming soon!", Toast.LENGTH_SHORT).show() },
                onAbout    = { Toast.makeText(context, "FitTrack Pro v1.0", Toast.LENGTH_SHORT).show() }
            )
        }

        composable(Routes.BMI_CALC)     { BMICalculatorScreen(fitTrackVM, onBack = { navController.popBackStack() }) }
        composable(Routes.WATER)        { WaterTrackerScreen(fitTrackVM,   onBack = { navController.popBackStack() }) }
        composable(Routes.SEARCH)       { SearchScreen(fitTrackVM,          onBack = { navController.popBackStack() }) }
        composable(Routes.ACHIEVEMENTS) { AchievementsScreen(onBack = { navController.popBackStack() }) }

        composable(Routes.SETTINGS)     {
            SettingsScreen(fitTrackVM,
                onBack = { navController.popBackStack() },
                onLogout = {
                    fitTrackVM.logout()
                    navController.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } }
                }
            )
        }

        composable(Routes.EDIT_PROFILE) {
            EditProfileScreen(fitTrackVM, onBack = { navController.popBackStack() })
        }
    }
}
