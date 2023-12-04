package com.example.playlistmaker.data.db

import com.example.playlistmaker.domain.model.Track

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

}