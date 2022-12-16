package com.jerry.patient.assessment.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jerry.patient.assessment.R
import com.jerry.patient.assessment.core.LocalAppBarTitle
import com.jerry.patient.assessment.service.data.VisitsDto
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun FormMain(
    patientInfo: VisitsDto,
    navController: DestinationsNavigator,
    viewModel: FormViewModel = koinViewModel()
) {
    LocalAppBarTitle.current(stringResource(R.string.feedback))

    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Feedback Form")
    }
}