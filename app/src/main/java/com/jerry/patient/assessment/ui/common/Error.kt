package com.jerry.patient.assessment.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jerry.patient.assessment.R
import com.jerry.patient.assessment.ui.common.theme.PatientTheme
import com.jerry.patient.assessment.ui.common.theme.ThemePreviews

@Composable
fun ErrorIndicator(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(44.dp),
            painter = painterResource(R.drawable.ic_baseline_error_outline_24),
            contentDescription = null
        )
        Text(text = stringResource(R.string.error_message))
        MediumButton(
            text = stringResource(R.string.retry),
            onClick = onRetry
        )
    }
}

@ThemePreviews
@Composable
private fun ErrorPreview() {
    PatientTheme {
        ErrorIndicator {}
    }
}