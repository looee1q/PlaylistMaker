package com.example.playlistmaker.ui.mediateca.playlists.model

import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist

data class PlaylistInfo(
    val playlist: Playlist,
    val duration: String
)