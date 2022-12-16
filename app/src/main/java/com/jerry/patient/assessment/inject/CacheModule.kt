package com.jerry.patient.assessment.inject

import androidx.room.Room
import com.jerry.patient.assessment.cache.FeedbackDatabase
import org.koin.dsl.module

val cacheModule = module {
    single {
        Room.databaseBuilder(get(), FeedbackDatabase::class.java, FeedbackDatabase.DATABASE_NAME)
            .build()
    }
    single { get<FeedbackDatabase>().feedbackDao() }
}