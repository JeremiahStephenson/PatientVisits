package com.jerry.patient.assessment.form

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import com.jerry.patient.assessment.cache.Feedback
import androidx.compose.runtime.*

@Stable
class FormState(
    feedBackChangedState: State<Boolean>,
    feedBackState: State<Feedback>
) {
    val feedBackChanged by feedBackChangedState
    val feedBack by feedBackState
}