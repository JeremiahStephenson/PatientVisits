package com.jerry.patient.assessment.ui.form

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import com.jerry.patient.assessment.cache.Feedback

@Stable
class FormUiState(
    feedBackChangedState: State<Boolean>,
    feedBackState: State<Feedback?>
) {
    val feedBackChanged by feedBackChangedState
    val feedBack by feedBackState
}