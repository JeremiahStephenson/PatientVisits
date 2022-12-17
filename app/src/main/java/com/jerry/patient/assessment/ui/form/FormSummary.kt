package com.jerry.patient.assessment.ui.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jerry.patient.assessment.R
import com.jerry.patient.assessment.cache.Feedback
import com.jerry.patient.assessment.ui.common.theme.Pink500

@Composable
fun Summary(
    results: Feedback
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.form_result_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        // rating
        Row(
            modifier = Modifier
                .padding(top = 24.dp)
                .semantics(mergeDescendants = true) {

                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.form_result_rating),
                style = MaterialTheme.typography.titleLarge
            )
            Icon(
                modifier = Modifier.padding(start = 12.dp),
                imageVector = Icons.Default.Favorite,
                tint = Pink500,
                contentDescription = stringResource(R.string.rating_accessibility, results.rating)
            )
            Text(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clearAndSetSemantics { },
                text = "x ${results.rating}"
            )
        }

        // understanding
        Row(
            modifier = Modifier.semantics(mergeDescendants = true) {  },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.form_result_understanding),
                style = MaterialTheme.typography.titleLarge
            )
            val contentDescription = stringResource(when (results.understanding) {
                true -> R.string.positive
                else -> R.string.negative
            })
            LikeButton(
                modifier = Modifier.semantics {
                    this.contentDescription = contentDescription
                },
                clickable = false,
                selected = false,
                up = results.understanding == true,
                tint = when (results.understanding) {
                    true -> Color.Green
                    else -> Color.Red
                }
            )
        }

        // feedback
        Text(
            text = stringResource(R.string.form_result_feedback),
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = results.feedback.orEmpty()
        )
    }
}