package com.example.playlistmaker

import android.content.res.Resources
import android.util.TypedValue
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

fun roundedCorners(radius: Float, resources: Resources) = RoundedCorners(
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        radius,
        resources.displayMetrics
    ).toInt()
)

fun showNumberOfTracksInCorrectDeclension(numberOfTracks: Int): String {
    return when {
        (numberOfTracks%100) >= 11 && (numberOfTracks%100) <= 19 -> "$numberOfTracks треков"
        (numberOfTracks%10) == 1 -> "$numberOfTracks трек"
        (numberOfTracks%10) >= 2 && (numberOfTracks%10) <= 4 -> "$numberOfTracks трека"
        else -> "$numberOfTracks треков"
    }
}