package com.example.playlistmaker.ui.models

@kotlinx.serialization.Serializable
data class TrackRepresentation(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val previewUrl: String,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseYear: String,
    val country: String,
    val genre: String
) {
    val artworkUrl512: String
        get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}