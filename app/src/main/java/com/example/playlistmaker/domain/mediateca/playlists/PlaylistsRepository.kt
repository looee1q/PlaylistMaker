package com.example.playlistmaker.domain.mediateca.playlists

import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun createPlaylist(playlist: Playlist)

    fun showPlaylists(): Flow<List<Playlist>>

    suspend fun updatePlaylist(playlist: Playlist, track: Track)

    suspend fun addTrackToPlaylistsTracksStorage(track: Track)

    suspend fun removeTrackFromPlaylistsTracksStorage(track: Track)

    suspend fun getPlaylist(playlistId: Int): Playlist

    fun getAllTracksFromPlaylist(tracksId: List<Long>): Flow<List<Track>>
}