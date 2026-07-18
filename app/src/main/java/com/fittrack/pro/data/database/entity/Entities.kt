package com.fittrack.pro.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId        : String,
    val name          : String,
    val type          : String,
    val durationMins  : Int,
    val caloriesBurned: Int,
    val distanceKm    : Float   = 0f,
    val notes         : String  = "",
    val date          : String,
    val time          : String,
    val timestamp     : Long    = System.currentTimeMillis(),
    val synced        : Boolean = false
)

@Entity(tableName = "daily_stats")
data class DailyStatsEntity(
    @PrimaryKey val date   : String,
    val userId             : String,
    val steps              : Int   = 0,
    val caloriesBurned     : Int   = 0,
    val workoutMinutes     : Int   = 0,
    val distanceKm         : Float = 0f,
    val waterGlasses       : Int   = 0,
    val stepGoal           : Int   = 10000,
    val calorieGoal        : Int   = 500
)

@Entity(tableName = "water_intake")
data class WaterIntakeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId     : String,
    val date       : String,
    val glasses    : Int  = 0,
    val goalGlasses: Int  = 8,
    val timestamp  : Long = System.currentTimeMillis()
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id : String,
    val userId         : String,
    val title          : String,
    val description    : String,
    val icon           : String,
    val unlockedAt     : Long?   = null,
    val isUnlocked     : Boolean = false
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val userId : String,
    val name               : String  = "",
    val email              : String  = "",
    val photoUrl           : String  = "",
    val heightCm           : Float   = 170f,
    val weightKg           : Float   = 70f,
    val age                : Int     = 25,
    val gender             : String  = "Male",
    val fitnessGoal        : String  = "Stay Fit",
    val isDarkMode         : Boolean = false
)
