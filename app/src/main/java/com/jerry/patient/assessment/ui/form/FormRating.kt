package com.jerry.patient.assessment.ui.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jerry.patient.assessment.R
import com.jerry.patient.assessment.ui.common.RatingBar
import com.jerry.patient.assessment.extensions.givenName
import com.jerry.patient.assessment.service.data.VisitsDto

@Composable
fun Rating(
    visitInfo: VisitsDto,
    isCurrentPage: Boolean,
    getRating: () -> Int,
    onRated: (Int) -> Unit,
    onEnableOrDisableContinue: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.semantics { heading() },
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            text = stringResource(
                R.string.form_question_1_1,
                visitInfo.patient.name.givenName,
                visitInfo.doctor.name.givenName
            )
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            text = stringResource(R.string.form_question_1_2)
        )
        Text(
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            text = stringResource(R.string.form_question_1_3)
        )
        LaunchedEffect(isCurrentPage) {
            if (isCurrentPage) {
                onEnableOrDisableContinue(getRating() > 0)
            }
        }

        RatingBar(
            rating = getRating(),
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        ) {
            onRated(it)
            onEnableOrDisableContinue(it > 0)
        }
    }
}