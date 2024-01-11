package com.example.playlistmaker.domain.mediateca.playlists.model

import android.net.Uri

data class Playlist(
    val title: String,
    val description: String?,
    val coverUri: Uri?,
    val tracksId: List<Long> = emptyList(),
) {
    val size: Int get() = tracksId.size
    var id = -1
}