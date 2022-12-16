package com.jerry.patient.assessment.service

import com.jerry.patient.assessment.cache.Feedback
import com.jerry.patient.assessment.service.data.VisitsDto
import java.io.Serializable

data class VisitsData(
    val visits: VisitsDto,
    val feedback: Feedback?
) : Serializable