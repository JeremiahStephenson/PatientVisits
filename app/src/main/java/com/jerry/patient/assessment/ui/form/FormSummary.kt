package com.jerry.patient.assessment.ui.form

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jerry.patient.assessment.R
import com.jerry.patient.assessment.cache.Feedback
import com.jerry.patient.assessment.ui.common.LikeButton
import com.jerry.patient.assessment.ui.common.theme.Pink500

@Composable
fun Summary(
    results: Feedback,
    onImageSelected: (Uri?) -> Unit
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
        Rating(results.rating)
        Understanding(results.understanding)
        FeedbackText(results.feedback.orEmpty())
        UserImage(results.image, onImageSelected)
    }
}

@Composable
private fun UserImage(
    imagePath: String?,
    onImageSelected: (Uri?) -> Unit
) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                onImageSelected(uri)
            }
        }
    OutlinedButton(
        modifier = Modifier.padding(top = 16.dp),
        onClick = {
            launcher.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }
    ) {
        Text(
            text = stringResource(
                when (imagePath) {
                    null -> R.string.load_image
                    else -> R.string.change_image
                }
            )
        )
    }
    AnimatedVisibility(visible = imagePath != null) {
        AsyncImage(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .fillMaxWidth()
                .height(200.dp)
                .clip(MaterialTheme.shapes.large),
            model = ImageRequest.Builder(LocalContext.current)
                .data(imagePath)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun FeedbackText(feedback: String) {
    Text(
        text = stringResource(R.string.form_result_feedback),
        style = MaterialTheme.typography.titleLarge
    )
    Text(
        modifier = Modifier.padding(top = 4.dp),
        text = feedback
    )
}

@Composable
private fun Understanding(understanding: Boolean?) {
    Row(
        modifier = Modifier.semantics(mergeDescendants = true) { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.form_result_understanding),
            style = MaterialTheme.typography.titleLarge
        )
        val contentDescription = stringResource(
            when (understanding) {
                true -> R.string.positive
                else -> R.string.negative
            }
        )
        LikeButton(
            modifier = Modifier.semantics {
                this.contentDescription = contentDescription
            },
            clickable = false,
            selected = false,
            up = understanding == true,
            tint = when (understanding) {
                true -> Color.Green
                else -> Color.Red
            }
        )
    }
}

@Composable
private fun Rating(rating: Int) {
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
            contentDescription = stringResource(R.string.rating_accessibility, rating)
        )
        Text(
            modifier = Modifier
                .padding(start = 4.dp)
                .clearAndSetSemantics { },
            text = "x ${rating}"
        )
    }
}