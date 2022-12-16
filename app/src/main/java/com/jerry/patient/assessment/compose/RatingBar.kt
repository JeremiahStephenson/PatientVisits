package com.jerry.patient.assessment.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jerry.patient.assessment.R
import com.jerry.patient.assessment.compose.theme.Pink500
import com.jerry.patient.assessment.extensions.unboundClickable

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
            RatingItem(
                contentDescription = "${it + 1}",
                filled = true
            ) {
                onRatingChange(it + 1)
            }
        }
        items(stars - rating) {
            RatingItem(
                contentDescription = "${it + 1}",
                filled = false
            ) {
                onRatingChange(rating + it + 1)
            }
        }
    }
}

@Composable
private fun RatingItem(
    contentDescription: String,
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
        contentDescription = contentDescription,
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