package com.jerry.patient.assessment.core

import androidx.compose.runtime.compositionLocalOf

val LocalAppBarTitle = compositionLocalOf<(String) -> Unit> { {} }