package com.example.playlistmaker.ui.mediateca.playlists.model

import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.ui.models.TrackRepresentation

data class PlaylistInfo(
    val playlist: Playlist,
    val tracks: List<TrackRepresentation>,
    val durationInMinutes: Int
)