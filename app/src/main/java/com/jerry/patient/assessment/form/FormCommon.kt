package com.jerry.patient.assessment.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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