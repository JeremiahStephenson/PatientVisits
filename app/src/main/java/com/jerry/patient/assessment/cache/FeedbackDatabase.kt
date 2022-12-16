package com.jerry.patient.assessment.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Feedback::class], version = 1, exportSchema = false)
abstract class FeedbackDatabase : RoomDatabase() {

    abstract fun feedbackDao(): FeedbackDao

    companion object {
        const val DATABASE_NAME = "patientFeedbackDb"
    }

}