package com.jerry.patient.assessment.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jerry.patient.assessment.ui.common.theme.PatientTheme

@Composable
fun MediumButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(text = text)
    }
}

@Composable
fun LikeButton(
    modifier: Modifier = Modifier,
    selected: Boolean,
    tint: Color,
    clickable: Boolean = true,
    up: Boolean = true,
    onSelected: () -> Unit = {}
) {
    Icon(
        modifier = modifier
            .rotate(if (up) 0F else 180F)
            .clip(CircleShape)
            .clickable(clickable) {
                onSelected()
            }
            .run {
                when (selected) {
                    true -> background(tint.copy(alpha = 0.3F))
                    else -> this
                }
            }
            .padding(16.dp),
        imageVector = Icons.Default.ThumbUp,
        contentDescription = null,
        tint = tint
    )
}

@Preview
@Composable
private fun MediumButtonPreview() {
    PatientTheme {
        MediumButton(text = "Preview") {}
    }
}

@Preview
@Composable
private fun LikeButtonSelectedPreview() {
    PatientTheme {
        LikeButton(
            selected = true,
            tint = Color.Green,
            up = true
        )
    }
}

@Preview
@Composable
private fun LikeButtonUnSelectedPreview() {
    PatientTheme {
        LikeButton(
            selected = false,
            tint = Color.Green,
            up = true
        )
    }
}

@Preview
@Composable
private fun DisLikeButtonSelectedPreview() {
    PatientTheme {
        LikeButton(
            selected = true,
            tint = Color.Red,
            up = false
        )
    }
}

@Preview
@Composable
private fun DisLikeButtonUnSelectedPreview() {
    PatientTheme {
        LikeButton(
            selected = false,
            tint = Color.Red,
            up = false
        )
    }
}