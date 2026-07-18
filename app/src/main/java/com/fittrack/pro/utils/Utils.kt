package com.fittrack.pro.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val isoFmt  = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val timeFmt = SimpleDateFormat("HH:mm",      Locale.getDefault())
    fun today()       = isoFmt.format(Date())
    fun currentTime() = timeFmt.format(Date())
}

object FitnessConstants {
    const val STEP_GOAL    = 10_000
    const val CALORIE_GOAL = 500
    const val WATER_GOAL   = 8
}

object CalorieEstimator {
    fun estimate(type: String, mins: Int): Int {
        val met = when (type.lowercase()) {
            "walking" -> 3.5; "running" -> 9.8; "cycling" -> 7.5
            "gym" -> 6.0; "yoga" -> 3.0; "swimming" -> 8.0
            "hiit" -> 12.0; "strength training" -> 5.0; else -> 5.0
        }
        return ((met * 70.0 * mins) / 60.0).toInt()
    }
}
