package com.example.playlistmaker.data.db

import androidx.core.net.toUri
import com.example.playlistmaker.data.db.entities.PlaylistEntity
import com.example.playlistmaker.data.db.entities.TrackEntity
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object TrackDBConvertor {

    fun convertTrackToTrackEntity(track: Track): TrackEntity {
        return TrackEntity(
            trackId = track.trackId.toString(),
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            previewUrl = track.previewUrl,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName.orEmpty(),
            releaseYear = track.releaseYear,
            country = track.country,
            genre = track.genre
        )
    }

    fun convertTrackEntityToTrack(trackEntity: TrackEntity): Track {
        return Track(
            trackId = trackEntity.trackId.toLong(),
            trackName = trackEntity.trackName,
            artistName = trackEntity.artistName,
            trackTime = trackEntity.trackTime,
            previewUrl = trackEntity.previewUrl,
            artworkUrl100 = trackEntity.artworkUrl100,
            collectionName = trackEntity.collectionName,
            releaseYear = trackEntity.releaseYear,
            country = trackEntity.country,
            genre = trackEntity.genre
        )
    }

    fun convertPlaylistToPlaylistEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            title = playlist.title,
            description = playlist.description.orEmpty(),
            coverUri = playlist.coverUri?.toString().orEmpty(),
            tracksId = Json.encodeToString(playlist.tracksId),
            size = playlist.size
        )
    }

    fun convertPlaylistEntityToPlaylist(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            title = playlistEntity.title,
            description = playlistEntity.description,
            coverUri = playlistEntity.coverUri.toUri(),
            tracksId = Json.decodeFromString(playlistEntity.tracksId),
        )
    }

}