package com.example.playlistmaker.data.mediateca

import android.util.Log
import com.example.playlistmaker.data.db.AppDB
import com.example.playlistmaker.data.db.DBConvertor
import com.example.playlistmaker.data.db.entities.PlaylistEntity
import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val appDB: AppDB
) : PlaylistsRepository {

    override suspend fun createPlaylist(playlist: Playlist) {
        appDB.playlistsDAO().addPlaylist(
            DBConvertor.convertPlaylistToPlaylistEntity(playlist)
        )
    }

    override fun showPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDB.playlistsDAO().showPlaylists().map {
            DBConvertor.convertPlaylistEntityToPlaylist(it)
        }
        emit(playlists)
    }

    override suspend fun updatePlaylist(playlist: Playlist, track: Track) {

        val updatedPlaylistTracksId = playlist.tracksId.toMutableList().also {
            it.add(track.trackId)
        }

        val playlistWithUpdatedTracksId = playlist.copy(
            tracksId = updatedPlaylistTracksId
        )

        appDB.playlistsDAO().updatePlaylist(
            DBConvertor.convertPlaylistToPlaylistEntity(playlistWithUpdatedTracksId)
        )
    }

    override suspend fun addTrackToPlaylistsTracksStorage(track: Track) {
        appDB.playlistsTracksDAO().addTrackToDB(
            DBConvertor.convertTrackToPlaylistTrackEntity(track)
        )
    }

    override suspend fun getPlaylist(playlistId: Int): Playlist {
        return DBConvertor.convertPlaylistEntityToPlaylist(
            appDB.playlistsDAO().getPlaylist(playlistId)
        )
    }

    override fun getAllTracksFromPlaylist(tracksId: List<Long>): Flow<List<Track>> = flow {
        val tracks = appDB.playlistsTracksDAO().getAllPlaylistsTracks().map {
            DBConvertor.convertTrackEntityToTrack(it)
        }.filter { tracksId.contains(it.trackId)  }

        emit(tracks)
    }

}