package com.fittrack.pro.data.database.dao

import androidx.room.*
import com.fittrack.pro.data.database.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: WorkoutEntity): Long

    @Update
    suspend fun update(workout: WorkoutEntity)

    @Delete
    suspend fun delete(workout: WorkoutEntity)

    @Query("SELECT * FROM workouts WHERE userId = :uid ORDER BY timestamp DESC")
    fun getAllWorkouts(uid: String): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE userId = :uid AND date = :date ORDER BY timestamp DESC")
    fun getWorkoutsByDate(uid: String, date: String): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE userId = :uid AND (name LIKE '%' || :q || '%' OR type LIKE '%' || :q || '%') ORDER BY timestamp DESC")
    fun searchWorkouts(uid: String, q: String): Flow<List<WorkoutEntity>>

    @Query("SELECT COUNT(*) FROM workouts WHERE userId = :uid")
    suspend fun getTotalCount(uid: String): Int

    @Query("SELECT SUM(caloriesBurned) FROM workouts WHERE userId = :uid")
    suspend fun getTotalCalories(uid: String): Int?

    @Query("SELECT MAX(durationMins) FROM workouts WHERE userId = :uid")
    suspend fun getLongest(uid: String): Int?

    @Query("SELECT AVG(durationMins) FROM workouts WHERE userId = :uid")
    suspend fun getAverage(uid: String): Float?

    @Query("UPDATE workouts SET synced = 1 WHERE id = :id")
    suspend fun markSynced(id: Int)
}

@Dao
interface DailyStatsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stats: DailyStatsEntity)

    @Query("SELECT * FROM daily_stats WHERE userId = :uid AND date = :date")
    fun getTodayStats(uid: String, date: String): Flow<DailyStatsEntity?>

    @Query("SELECT * FROM daily_stats WHERE userId = :uid ORDER BY date DESC LIMIT 7")
    fun getLastSevenDays(uid: String): Flow<List<DailyStatsEntity>>

    @Query("SELECT * FROM daily_stats WHERE userId = :uid ORDER BY date DESC LIMIT 30")
    fun getLastThirtyDays(uid: String): Flow<List<DailyStatsEntity>>
}

@Dao
interface WaterIntakeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(water: WaterIntakeEntity)

    @Update
    suspend fun update(water: WaterIntakeEntity)

    @Query("SELECT * FROM water_intake WHERE userId = :uid AND date = :date LIMIT 1")
    fun getTodayWater(uid: String, date: String): Flow<WaterIntakeEntity?>
}

@Dao
interface AchievementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(achievement: AchievementEntity)

    @Query("SELECT * FROM achievements WHERE userId = :uid")
    fun getAll(uid: String): Flow<List<AchievementEntity>>
}

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: UserProfileEntity)

    @Update
    suspend fun update(profile: UserProfileEntity)

    @Query("SELECT * FROM user_profile WHERE userId = :uid LIMIT 1")
    fun getProfile(uid: String): Flow<UserProfileEntity?>
}
