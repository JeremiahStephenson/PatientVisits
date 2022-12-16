package com.jerry.patient.assessment.core

import com.jerry.patient.assessment.service.data.NameDto

val List<NameDto>.givenName get() = firstOrNull()?.given?.firstOrNull().orEmpty()