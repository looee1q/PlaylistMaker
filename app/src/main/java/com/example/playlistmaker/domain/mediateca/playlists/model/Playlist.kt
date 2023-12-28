package com.example.playlistmaker.domain.mediateca.playlists.model

import android.net.Uri

data class Playlist(
    //val id: String,
    val title: String,
    val description: String?,
    val coverUri: Uri?,
    val tracksId: List<Long> = emptyList(),
    val size: Int = 0
)