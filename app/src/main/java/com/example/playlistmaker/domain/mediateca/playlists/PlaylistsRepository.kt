package com.example.playlistmaker.domain.mediateca.playlists

import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun createPlaylist(playlist: Playlist)

    fun showPlaylists(): Flow<List<Playlist>>

    suspend fun updatePlaylist(playlist: Playlist)
}