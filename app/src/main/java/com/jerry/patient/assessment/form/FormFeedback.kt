package com.jerry.patient.assessment.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jerry.patient.assessment.R
import com.jerry.patient.assessment.service.data.VisitsDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Feedback(
    visitInfo: VisitsDto,
    isCurrentPage: Boolean,
    getFeedback: () -> String?,
    onFeedback: (String?) -> Unit,
    onEnableOrDisableContinue: (Boolean) -> Unit
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
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            text = stringResource(
                R.string.form_question_3,
                visitInfo.diagnosis.code.diagnosis.lowercase(),
            )
        )
        var text by rememberSaveable { mutableStateOf(getFeedback().orEmpty()) }
        val length = remember { derivedStateOf { text.length > MIN_LENGTH } }
        LaunchedEffect(isCurrentPage, length.value) {
            if (isCurrentPage) {
                onEnableOrDisableContinue(length.value)
            }
        }
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp)
                .fillMaxWidth()
                .height(200.dp),
            value = text,
            onValueChange = {
                if (it.length < MAX_LENGTH) {
                    text = it
                }
                onFeedback(
                    when (it.length in (MIN_LENGTH + 1) until MAX_LENGTH) {
                        true -> it
                        else -> null
                    }
                )
            },
            placeholder = {
                Text(text = stringResource(R.string.feedback))
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            )
        )
        Text(
            modifier = Modifier
                .padding(top = 4.dp)
                .padding(horizontal = 24.dp),
            style = MaterialTheme.typography.labelSmall,
            text = stringResource(R.string.min_char_requirement)
        )
    }
}

private const val MAX_LENGTH = 300
private const val MIN_LENGTH = 30