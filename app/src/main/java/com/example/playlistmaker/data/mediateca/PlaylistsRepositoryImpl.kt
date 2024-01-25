package com.example.playlistmaker.data.mediateca

import com.example.playlistmaker.data.db.AppDB
import com.example.playlistmaker.data.db.DBConvertor
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

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDB.playlistsDAO().deletePlaylist(
            DBConvertor.convertPlaylistToPlaylistEntity(playlist)
        )
        playlist.tracksId.forEach {
            removeTrackFromPlaylistsTracks(it, playlist)
        }
    }

    override fun showPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDB.playlistsDAO().showPlaylists().map {
            DBConvertor.convertPlaylistEntityToPlaylist(it)
        }
        emit(playlists)
    }

    override suspend fun removeTrackFromPlaylist(track: Track, playlist: Playlist) {
        removeTrackFromPlaylistsTracks(track, playlist)
        updatePlaylist(playlist, track)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        addTrackToPlaylistsTracksStorage(track)
        updatePlaylist(playlist, track)
    }

    override suspend fun updatePlaylist(playlist: Playlist, track: Track) {

        val updatedPlaylistTracksId = if (playlist.tracksId.contains(track.trackId)) {
            playlist.tracksId.toMutableList().also {
                it.remove(track.trackId)
            }
        } else {
            playlist.tracksId.toMutableList().also {
                it.add(track.trackId)
            }
        }

        val playlistWithUpdatedTracksId = playlist.copy(
            tracksId = updatedPlaylistTracksId
        )

        appDB.playlistsDAO().updatePlaylist(
            DBConvertor.convertPlaylistToPlaylistEntity(playlistWithUpdatedTracksId)
        )
    }

    private suspend fun addTrackToPlaylistsTracksStorage(track: Track) {
        appDB.playlistsTracksDAO().addTrackToDB(
            DBConvertor.convertTrackToPlaylistTrackEntity(track)
        )
    }

    private suspend fun removeTrackFromPlaylistsTracks(track: Track, playlistWithDeletedTrack: Playlist) {
        removeTrackFromPlaylistsTracks(track.trackId, playlistWithDeletedTrack)
    }
    private suspend fun removeTrackFromPlaylistsTracks(trackId: Long, playlistWithDeletedTrack: Playlist) {
        showPlaylists().collect{
            var somePlaylistsContainTrack = false

            it.forEach {
                if (it.tracksId.contains(trackId) && it.id != playlistWithDeletedTrack.id) {
                    somePlaylistsContainTrack = true
                }
            }

            if(!somePlaylistsContainTrack) {
                appDB.playlistsTracksDAO().removeTrackFromDB(trackId)
            }
        }
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