package com.fittrack.pro.data.database

import android.content.Context
import androidx.room.*
import com.fittrack.pro.data.database.dao.*
import com.fittrack.pro.data.database.entity.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Database(
    entities = [WorkoutEntity::class, DailyStatsEntity::class,
                WaterIntakeEntity::class, AchievementEntity::class, UserProfileEntity::class],
    version  = 1,
    exportSchema = false
)
abstract class FitTrackDatabase : RoomDatabase() {
    abstract fun workoutDao()    : WorkoutDao
    abstract fun dailyStatsDao() : DailyStatsDao
    abstract fun waterIntakeDao(): WaterIntakeDao
    abstract fun achievementDao(): AchievementDao
    abstract fun userProfileDao(): UserProfileDao


    companion object {
        @Volatile private var INSTANCE: FitTrackDatabase? = null
        fun getInstance(context: Context): FitTrackDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, FitTrackDatabase::class.java, "fittrack_db")
                    .fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
    }
    // Inside FitTrackViewModel.kt

    private val _userName = MutableStateFlow("FitTrack User")
    val userName = _userName.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode = _isDarkMode.asStateFlow()

    fun updateUserName(newName: String) {
        _userName.value = newName
    }

    fun toggleDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
    }


}
