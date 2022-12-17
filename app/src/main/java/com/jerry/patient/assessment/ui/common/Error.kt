package com.jerry.patient.assessment.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.jerry.patient.assessment.R

@Composable
fun ErrorIndicator(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
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