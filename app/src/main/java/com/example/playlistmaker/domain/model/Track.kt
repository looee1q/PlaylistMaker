package com.example.playlistmaker.domain.model

data class Track(
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
)