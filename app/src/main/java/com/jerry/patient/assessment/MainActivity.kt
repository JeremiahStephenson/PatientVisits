package com.jerry.patient.assessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.jerry.patient.assessment.ui.common.theme.PatientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            PatientTheme {
                Content { onBackPressedDispatcher.onBackPressed() }
            }
        }
    }
}
