package com.jerry.patient.assessment.compose

import androidx.compose.runtime.compositionLocalOf

val LocalAppBarTitle = compositionLocalOf<(String) -> Unit> { {} }