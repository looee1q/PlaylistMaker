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