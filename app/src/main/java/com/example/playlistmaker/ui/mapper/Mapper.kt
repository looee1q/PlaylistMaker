package com.example.playlistmaker.ui.mapper

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.models.TrackRepresentation

object Mapper {
    fun mapTrackToTrackRepresentation(track: Track) : TrackRepresentation {
        return TrackRepresentation(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            previewUrl = track.previewUrl,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseYear = track.releaseYear,
            country = track.country,
            genre = track.genre
        )
    }
    fun mapTrackRepresentationToTrack(trackRepresentation: TrackRepresentation) : Track {
        return Track(
            trackId = trackRepresentation.trackId,
            trackName = trackRepresentation.trackName,
            artistName = trackRepresentation.artistName,
            trackTime = trackRepresentation.trackTime,
            previewUrl = trackRepresentation.previewUrl,
            artworkUrl100 = trackRepresentation.artworkUrl100,
            collectionName = trackRepresentation.collectionName,
            releaseYear = trackRepresentation.releaseYear,
            country = trackRepresentation.country,
            genre = trackRepresentation.genre
        )
    }
}