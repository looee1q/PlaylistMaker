package com.example.playlistmaker.presentation.mapper

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.models.TrackActivity

object TrackToTrackActivityMapper {
    fun map(track: Track) : TrackActivity {
        return TrackActivity(
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
}