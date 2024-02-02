package com.example.playlistmaker.data.mediateca

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import com.example.playlistmaker.data.db.AppDB
import com.example.playlistmaker.data.db.DBConvertor
import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream

class PlaylistsRepositoryImpl(
    private val appDB: AppDB,
    private val context: Context
) : PlaylistsRepository {

    override suspend fun createPlaylist(playlist: Playlist) {
        Log.d("PlaylistRepositoryImpl","Буду создавать новый плейлист!")
        appDB.playlistsDAO().addPlaylist(
            DBConvertor.convertPlaylistToPlaylistEntity(playlist)
        )
        Log.d("PlaylistRepositoryImpl","Создал новый плейлист!")
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDB.playlistsDAO().deletePlaylist(
            DBConvertor.convertPlaylistToPlaylistEntity(playlist)
        )
        playlist.tracksId.forEach {
            removeTrackFromPlaylistsTracks(it, playlist)
        }
        playlist.coverUri?.let {
            deleteCoverFromExternalStorage(it.toString())
        }
    }

    override fun showPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDB.playlistsDAO().showPlaylists().map {
            DBConvertor.convertPlaylistEntityToPlaylist(it)
        }
        emit(playlists)
        Log.d("PlaylistRepositoryImpl","Эмичу плейлисты!")
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

    override suspend fun updatePlaylist(playlistWithUpdatedData: Playlist) {
        appDB.playlistsDAO().updatePlaylist(
            DBConvertor.convertPlaylistToPlaylistEntity(playlistWithUpdatedData)
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

    override suspend fun saveCoverToExternalStorage(uri: String): String {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "PlaylistsCovers")
        if (!filePath.exists()) filePath.mkdirs()

        var numberOfSavedCover = if(filePath.listFiles().isEmpty()) {
            1
        } else {
            filePath.listFiles().last().path
                .substringAfterLast("cover_")
                .substringBefore(".jpg")
                .toInt() + 1
        }

        var coverFile = File(filePath, "cover_$numberOfSavedCover.jpg")

        while (coverFile.exists()) {
            numberOfSavedCover++
            coverFile = File(filePath, "cover_$numberOfSavedCover.jpg")
        }

        val uriToSaveInExternalStorage = coverFile.toUri()

        val inputStream = context.contentResolver.openInputStream(uri.toUri())
        val outputStream = FileOutputStream(coverFile)
        BitmapFactory.decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

        return uriToSaveInExternalStorage.toString()
    }

    override suspend fun deleteCoverFromExternalStorage(uri: String) {
        uri.toUri().path?.let { File(it).delete() }
    }

}