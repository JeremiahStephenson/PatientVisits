package com.jerry.patient.assessment.cache

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = Feedback.TABLE_NAME,
    indices = [(Index(value = ["visitId"], unique = true))]
)
data class Feedback(
    var visitId: String = "",
    var rating: Int = 0,
    var understanding: Boolean? = null,
    var feedback: String? = null
) : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

    companion object {
        const val TABLE_NAME = "feedback"
    }
}