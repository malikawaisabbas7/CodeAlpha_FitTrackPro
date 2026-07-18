package com.fittrack.pro.data.repository

import com.fittrack.pro.data.database.dao.*
import com.fittrack.pro.data.database.entity.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class WorkoutRepository(
    private val dao      : WorkoutDao,
    private val firestore: FirebaseFirestore,
    private val auth     : FirebaseAuth
) {
    private val uid get() = auth.currentUser?.uid ?: ""

    fun getAll()                        = dao.getAllWorkouts(uid)
    fun getByDate(date: String)         = dao.getWorkoutsByDate(uid, date)
    fun search(query: String)           = dao.searchWorkouts(uid, query)
    suspend fun getTotalCount()         = dao.getTotalCount(uid)
    suspend fun getTotalCalories()      = dao.getTotalCalories(uid) ?: 0
    suspend fun getLongest()            = dao.getLongest(uid) ?: 0
    suspend fun getAverage()            = dao.getAverage(uid) ?: 0f

    suspend fun add(workout: WorkoutEntity): Long {
        val id = dao.insert(workout.copy(userId = uid))
        try {
            firestore.collection("users").document(uid)
                .collection("workouts").document(id.toString())
                .set(workout.copy(userId = uid, synced = true)).await()
            dao.markSynced(id.toInt())
        } catch (_: Exception) { }
        return id
    }

    suspend fun update(workout: WorkoutEntity) {
        dao.update(workout)
        try { firestore.collection("users").document(uid).collection("workouts")
            .document(workout.id.toString()).set(workout).await() } catch (_: Exception) { }
    }

    suspend fun delete(workout: WorkoutEntity) {
        dao.delete(workout)
        try { firestore.collection("users").document(uid).collection("workouts")
            .document(workout.id.toString()).delete().await() } catch (_: Exception) { }
    }
}

class DailyStatsRepository(
    private val dao      : DailyStatsDao,
    private val firestore: FirebaseFirestore,
    private val auth     : FirebaseAuth
) {
    private val uid get() = auth.currentUser?.uid ?: ""

    fun getTodayStats(date: String)  = dao.getTodayStats(uid, date)
    fun getLastSevenDays()           = dao.getLastSevenDays(uid)

    suspend fun updateStats(stats: DailyStatsEntity) {
        dao.insert(stats.copy(userId = uid))
        try { firestore.collection("users").document(uid)
            .collection("daily_stats").document(stats.date).set(stats).await() } catch (_: Exception) { }
    }
}

class WaterRepository(
    private val dao : WaterIntakeDao,
    private val auth: FirebaseAuth
) {
    private val uid get() = auth.currentUser?.uid ?: ""

    fun getTodayWater(date: String) = dao.getTodayWater(uid, date)

    suspend fun addGlass(date: String, current: WaterIntakeEntity?) {
        val updated = current?.copy(glasses = current.glasses + 1)
            ?: WaterIntakeEntity(userId = uid, date = date, glasses = 1)
        dao.insert(updated)
    }

    suspend fun removeGlass(current: WaterIntakeEntity?) {
        if (current != null && current.glasses > 0) dao.update(current.copy(glasses = current.glasses - 1))
    }
}

class AuthRepository(
    private val auth     : FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    val currentUser get() = auth.currentUser

    suspend fun login(email: String, password: String): Result<Unit> = try {
        auth.signInWithEmailAndPassword(email, password).await(); Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    suspend fun register(email: String, password: String, name: String): Result<Unit> = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        result.user?.let {
            firestore.collection("users").document(it.uid)
                .set(mapOf("name" to name, "email" to email, "createdAt" to System.currentTimeMillis())).await()
        }
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    suspend fun forgotPassword(email: String): Result<Unit> = try {
        auth.sendPasswordResetEmail(email).await(); Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    fun logout() = auth.signOut()
}
