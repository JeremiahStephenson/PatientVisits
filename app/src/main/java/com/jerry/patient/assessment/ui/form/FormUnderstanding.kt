package com.jerry.patient.assessment.ui.form

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jerry.patient.assessment.R
import com.jerry.patient.assessment.ui.common.LikeButton

@Composable
fun Understanding(
    diagnosis: String,
    doctorName: String,
    isCurrentPage: Boolean,
    getUnderstanding: () -> Boolean?,
    onUnderstanding: (Boolean) -> Unit,
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
            style = MaterialTheme.typography.titleLarge,
            text = stringResource(R.string.thank_you)
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            text = stringResource(
                R.string.form_question_2,
                diagnosis,
                doctorName
            )
        )
        SideEffect {
            if (isCurrentPage) {
                onEnableOrDisableContinue(getUnderstanding() != null)
            }
        }
        LikeButtons(positive = getUnderstanding()) {
            onUnderstanding(it)
        }
    }
}

@Composable
private fun LikeButtons(
    positive: Boolean?,
    onSelected: (Boolean) -> Unit
) {
    Row(modifier = Modifier.padding(24.dp)) {
        val positiveContentDescription = stringResource(R.string.positive)
        LikeButton(
            modifier = Modifier.semantics {
                contentDescription = positiveContentDescription
            },
            selected = positive == true,
            tint = Color.Green
        ) {
            onSelected(true)
        }
        Spacer(modifier = Modifier.width(50.dp))
        val negativeContentDescription = stringResource(R.string.negative)
        LikeButton(
            modifier = Modifier.semantics {
                contentDescription = negativeContentDescription
            },
            selected = positive == false,
            tint = Color.Red,
            up = false
        ) {
            onSelected(false)
        }
    }
}

@Preview(widthDp = 400, heightDp = 400)
@Composable
private fun UnderstandingPreview() {
    Understanding(
        diagnosis = "Lupus",
        doctorName = "Gregory House",
        isCurrentPage = true,
        getUnderstanding = { true },
        onUnderstanding = {},
        onEnableOrDisableContinue = {}
    )
}