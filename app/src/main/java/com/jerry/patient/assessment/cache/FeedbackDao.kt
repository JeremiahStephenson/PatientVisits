package com.jerry.patient.assessment.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedbackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(feedback: Feedback)

    @Query("SELECT * FROM feedback WHERE visitId = :visitId")
    fun findFeedbackByVisitId(visitId: String): Flow<Feedback?>
}