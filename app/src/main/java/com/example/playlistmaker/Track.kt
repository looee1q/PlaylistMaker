package com.example.playlistmaker

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: String,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: String,
    val country: String,
    @SerializedName("primaryGenreName") val genre: String
) : Serializable {
    val artworkUrl512: String
        get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    fun getDuration(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime.toLong())

    fun getYear(): String = releaseDate.substringBefore('-')
}