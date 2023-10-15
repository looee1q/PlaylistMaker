package com.example.playlistmaker.ui.mapper

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.models.TrackActivity

object Mapper {
    fun mapTrackToTrackActivity(track: Track) : TrackActivity {
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
    fun mapTrackActivityToTrack(trackActivity: TrackActivity) : Track {
        return Track(
            trackId = trackActivity.trackId,
            trackName = trackActivity.trackName,
            artistName = trackActivity.artistName,
            trackTime = trackActivity.trackTime,
            previewUrl = trackActivity.previewUrl,
            artworkUrl100 = trackActivity.artworkUrl100,
            collectionName = trackActivity.collectionName,
            releaseYear = trackActivity.releaseYear,
            country = trackActivity.country,
            genre = trackActivity.genre
        )
    }
}