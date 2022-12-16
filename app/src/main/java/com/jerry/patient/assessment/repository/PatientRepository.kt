package com.jerry.patient.assessment.repository

import com.jerry.patient.assessment.cache.Feedback
import com.jerry.patient.assessment.service.data.VisitsDto
import kotlinx.coroutines.flow.Flow

interface PatientRepository {
    suspend fun loadVisitsInfo(patientId: String): VisitsDto
    suspend fun saveFeedback(feedback: Feedback)
    fun feedbackFlow(visitId: String): Flow<Feedback?>
}