package com.example.playlistmaker.data.db

import androidx.core.net.toUri
import com.example.playlistmaker.data.db.entities.PlaylistEntity
import com.example.playlistmaker.data.db.entities.PlaylistTrackEntity
import com.example.playlistmaker.data.db.entities.TrackEntity
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object DBConvertor {

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

    fun convertTrackToPlaylistTrackEntity(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(
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

    fun convertPlaylistTrackEntityToTrack(playlistTrackEntity: PlaylistTrackEntity): Track {
        return Track(
            trackId = playlistTrackEntity.trackId.toLong(),
            trackName = playlistTrackEntity.trackName,
            artistName = playlistTrackEntity.artistName,
            trackTime = playlistTrackEntity.trackTime,
            previewUrl = playlistTrackEntity.previewUrl,
            artworkUrl100 = playlistTrackEntity.artworkUrl100,
            collectionName = playlistTrackEntity.collectionName,
            releaseYear = playlistTrackEntity.releaseYear,
            country = playlistTrackEntity.country,
            genre = playlistTrackEntity.genre
        )
    }

    fun convertPlaylistToPlaylistEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            title = playlist.title,
            description = playlist.description.orEmpty(),
            coverUri = playlist.coverUri?.toString().orEmpty(),
            tracksId = Json.encodeToString(playlist.tracksId),
            size = playlist.size
        ).also {
            if (playlist.id != -1) it.id = playlist.id
        }
    }

    fun convertPlaylistEntityToPlaylist(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            id = playlistEntity.id,
            title = playlistEntity.title,
            description = playlistEntity.description,
            coverUri = playlistEntity.coverUri.toUri(),
            tracksId = Json.decodeFromString(playlistEntity.tracksId),
        )
    }

}