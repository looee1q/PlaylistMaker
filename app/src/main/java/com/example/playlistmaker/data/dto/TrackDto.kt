package com.example.playlistmaker.data.dto

import kotlinx.serialization.SerialName
import java.text.SimpleDateFormat
import java.util.*

@kotlinx.serialization.Serializable
data class TrackDto(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    @SerialName("trackTimeMillis") val trackTime: Long,
    val previewUrl: String,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: String,
    val country: String,
    @SerialName("primaryGenreName") val genre: String
) {
    fun getDuration(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)

    fun getYear(): String = releaseDate.substringBefore('-')
}