package com.example.playlistmaker

import kotlinx.serialization.SerialName
import java.text.SimpleDateFormat
import java.util.*

@kotlinx.serialization.Serializable
data class Track(
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
    val artworkUrl512: String
        get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    fun getDuration(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)

    fun getYear(): String = releaseDate.substringBefore('-')
}