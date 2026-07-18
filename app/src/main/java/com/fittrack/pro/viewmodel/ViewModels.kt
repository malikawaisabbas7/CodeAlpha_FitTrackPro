package com.fittrack.pro.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.fittrack.pro.data.database.FitTrackDatabase
import com.fittrack.pro.data.database.entity.*
import com.fittrack.pro.data.repository.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FitTrackViewModel(application: Application) : AndroidViewModel(application) {

    private val db        = FitTrackDatabase.getInstance(application)
    private val auth      = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val today     = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private val workoutRepo = WorkoutRepository(db.workoutDao(), firestore, auth)
    private val statsRepo   = DailyStatsRepository(db.dailyStatsDao(), firestore, auth)
    private val waterRepo   = WaterRepository(db.waterIntakeDao(), auth)
    val authRepo            = AuthRepository(auth, firestore)

    // --- NEW: THEME & USERNAME STATE ---
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode = _isDarkMode.asStateFlow()

    private val _userName = MutableStateFlow("FitTrack User")
    val userName = _userName.asStateFlow()

    // --- FUNCTIONAL UPDATES ---
    fun toggleDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
    }

    fun updateUserName(newName: String) {
        if (newName.isNotBlank()) {
            _userName.value = newName
        }
    }

    // Streams
    val todayStats    = statsRepo.getTodayStats(today).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val todayWater    = waterRepo.getTodayWater(today).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val allWorkouts   = workoutRepo.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val todayWorkouts = workoutRepo.getByDate(today).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val weeklyStats   = statsRepo.getLastSevenDays().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Summary stats
    private val _totalWorkouts  = MutableStateFlow(0)
    private val _totalCalories  = MutableStateFlow(0)
    private val _longestWorkout = MutableStateFlow(0)
    private val _avgWorkout     = MutableStateFlow(0f)
    val totalWorkouts  : StateFlow<Int>   = _totalWorkouts
    val totalCalories  : StateFlow<Int>   = _totalCalories
    val longestWorkout : StateFlow<Int>   = _longestWorkout
    val avgWorkout     : StateFlow<Float> = _avgWorkout

    // Search
    private val _searchQuery = MutableStateFlow("")
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<WorkoutEntity>> = _searchQuery.debounce(300)
        .flatMapLatest { q -> if (q.isBlank()) workoutRepo.getAll() else workoutRepo.search(q) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(q: String) { _searchQuery.value = q }

    init { loadSummaryStats() }

    private fun loadSummaryStats() = viewModelScope.launch {
        _totalWorkouts.value  = workoutRepo.getTotalCount()
        _totalCalories.value  = workoutRepo.getTotalCalories()
        _longestWorkout.value = workoutRepo.getLongest()
        _avgWorkout.value     = workoutRepo.getAverage()
    }

    fun addWorkout(workout: WorkoutEntity) = viewModelScope.launch {
        workoutRepo.add(workout)
        // Update today's stats
        val current = statsRepo.getTodayStats(today).first()
        val updated = current?.copy(
            caloriesBurned = current.caloriesBurned + workout.caloriesBurned,
            workoutMinutes = current.workoutMinutes  + workout.durationMins
        ) ?: DailyStatsEntity(date = today, userId = auth.currentUser?.uid ?: "",
            caloriesBurned = workout.caloriesBurned, workoutMinutes = workout.durationMins)
        statsRepo.updateStats(updated)
        loadSummaryStats()
    }

    fun updateWorkout(workout: WorkoutEntity) = viewModelScope.launch { workoutRepo.update(workout) }
    fun deleteWorkout(workout: WorkoutEntity) = viewModelScope.launch { workoutRepo.delete(workout) }

    fun addWaterGlass()    = viewModelScope.launch { waterRepo.addGlass(today, todayWater.value) }
    fun removeWaterGlass() = viewModelScope.launch { waterRepo.removeGlass(todayWater.value) }

    fun updateSteps(steps: Int) = viewModelScope.launch {
        val current = statsRepo.getTodayStats(today).first()
        val updated = current?.copy(steps = steps)
            ?: DailyStatsEntity(date = today, userId = auth.currentUser?.uid ?: "", steps = steps)
        statsRepo.updateStats(updated)
    }

    fun calculateBMI(heightCm: Float, weightKg: Float): Float {
        if (heightCm <= 0) return 0f
        val h = heightCm / 100f
        return weightKg / (h * h)
    }

    fun getBMICategory(bmi: Float) = when {
        bmi < 18.5f -> "Underweight"
        bmi < 25f   -> "Normal"
        bmi < 30f   -> "Overweight"
        else        -> "Obese"
    }

    fun logout() = authRepo.logout()
}

// ── Auth ViewModel ────────────────────────────────────────────────────────────
class AuthViewModel : ViewModel() {
    private val auth      = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val repo      = AuthRepository(auth, firestore)

    sealed class AuthState {
        object Idle    : AuthState()
        object Loading : AuthState()
        object Success : AuthState()
        data class Error(val message: String) : AuthState()
    }

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _state
    val isLoggedIn get() = repo.currentUser != null

    fun login(email: String, password: String) = viewModelScope.launch {
        _state.value = AuthState.Loading
        repo.login(email, password).fold(
            onSuccess = { _state.value = AuthState.Success },
            onFailure = { _state.value = AuthState.Error(it.message ?: "Login failed") }
        )
    }

    fun register(email: String, password: String, name: String) = viewModelScope.launch {
        _state.value = AuthState.Loading
        repo.register(email, password, name).fold(
            onSuccess = { _state.value = AuthState.Success },
            onFailure = { _state.value = AuthState.Error(it.message ?: "Registration failed") }
        )
    }

    fun forgotPassword(email: String) = viewModelScope.launch {
        _state.value = AuthState.Loading
        repo.forgotPassword(email).fold(
            onSuccess = { _state.value = AuthState.Error("Reset email sent! Check your inbox ✓") },
            onFailure = { _state.value = AuthState.Error(it.message ?: "Failed") }
        )
    }

    fun resetState() { _state.value = AuthState.Idle }
}