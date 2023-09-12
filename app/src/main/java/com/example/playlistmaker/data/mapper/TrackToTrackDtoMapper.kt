package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.dto.ITunesServerResponse
import com.example.playlistmaker.domain.model.Track

object TrackToTrackDtoMapper {
    fun map(iTunesServerResponse: ITunesServerResponse): List<Track> {
        return iTunesServerResponse.results.map {
            Track(
                trackId = it.trackId,
                trackName = it.trackName,
                artistName = it.artistName,
                trackTime = it.getDuration(),
                previewUrl = it.previewUrl,
                artworkUrl100 = it.artworkUrl100,
                collectionName = it.collectionName,
                releaseYear = it.getYear(),
                country = it.country,
                genre = it.genre
            )

        }
    }
}