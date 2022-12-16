package com.jerry.patient.assessment.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jerry.patient.assessment.ui.theme.Pink500

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int = 0,
    stars: Int = 10,
    onRatingChange: (Int) -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        items(rating) {
            RatingItem(filled = true) {
                onRatingChange(it + 1)
            }
        }
        items(stars - rating) {
            RatingItem(filled = false) {
                onRatingChange(rating + it + 1)
            }
        }
    }
}

@Composable
private fun RatingItem(
    filled: Boolean = false,
    onRatingChange: () -> Unit
) {
    Icon(
        modifier = Modifier
            .padding(2.dp)
            .unboundClickable { onRatingChange() },
        imageVector = when (filled) {
            true -> Icons.Default.Favorite
            else -> Icons.Default.FavoriteBorder
        },
        contentDescription = null,
        tint = Pink500
    )
}


@Preview
@Composable
fun RatingPreview() {
    RatingBar(rating = 2) {}
}

@Preview
@Composable
fun TenStarsRatingPreview() {
    RatingBar(stars = 10, rating = 8) {}
}