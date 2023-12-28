package com.example.playlistmaker.data.mediateca

import com.example.playlistmaker.data.db.FavoriteTracksDB
import com.example.playlistmaker.data.db.TrackDBConvertor
import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val favoriteTracksDB: FavoriteTracksDB
) : PlaylistsRepository {

    override suspend fun createPlaylist(playlist: Playlist) {
        favoriteTracksDB.playlistsDAO().addPlaylist(
            TrackDBConvertor.convertPlaylistToPlaylistEntity(playlist)
        )
    }

    override fun showPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = favoriteTracksDB.playlistsDAO().showPlaylists().map {
            TrackDBConvertor.convertPlaylistEntityToPlaylist(it)
        }
        emit(playlists)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        favoriteTracksDB.playlistsDAO().updatePlaylist(
            TrackDBConvertor.convertPlaylistToPlaylistEntity(playlist)
        )
    }
}