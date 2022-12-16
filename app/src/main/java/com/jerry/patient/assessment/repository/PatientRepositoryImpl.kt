package com.jerry.patient.assessment.repository

import com.jerry.patient.assessment.cache.Feedback
import com.jerry.patient.assessment.cache.FeedbackDao
import com.jerry.patient.assessment.service.VisitsAPI

class PatientRepositoryImpl(
    private val visitsAPI: VisitsAPI,
    private val feedbackDao: FeedbackDao
) : PatientRepository {

    override suspend fun loadVisitsInfo(patientId: String) =
        visitsAPI.getVisitsInfo()

    override suspend fun saveFeedback(feedback: Feedback) =
        feedbackDao.insert(feedback)

    override fun feedbackFlow(visitId: String) =
        feedbackDao.findFeedbackByVisitId(visitId)
}