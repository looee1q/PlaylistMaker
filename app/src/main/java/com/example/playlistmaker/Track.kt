package com.example.playlistmaker

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: String,
    val artworkUrl100: String
) {
    fun getDuration(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime.toLong())
}