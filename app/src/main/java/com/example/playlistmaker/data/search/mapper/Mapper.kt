package com.example.playlistmaker.data.search.mapper

import com.example.playlistmaker.data.search.dto.ITunesServerResponse
import com.example.playlistmaker.data.search.dto.SimpleTrack
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.model.Track

object Mapper {
    fun mapITunesServerResponseToListOfTracks(iTunesServerResponse: ITunesServerResponse): List<Track> {
        return iTunesServerResponse.results.map {
            mapTrackDtoToTrack(it)
        }
    }

    fun mapTrackDtoToTrack(trackDto: TrackDto): Track {
        return Track(
            trackId = trackDto.trackId,
            trackName = trackDto.trackName,
            artistName = trackDto.artistName,
            trackTime = trackDto.getDuration(),
            previewUrl = trackDto.previewUrl,
            artworkUrl100 = trackDto.artworkUrl100,
            collectionName = trackDto.collectionName,
            releaseYear = trackDto.getYear(),
            country = trackDto.country,
            genre = trackDto.genre
        )
    }

    fun mapTrackToSimpleTrack(track: Track): SimpleTrack {
        return SimpleTrack(
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
    fun mapSimpeTrackToTrack(simpleTrack: SimpleTrack): Track {
        return Track(
            trackId = simpleTrack.trackId,
            trackName = simpleTrack.trackName,
            artistName = simpleTrack.artistName,
            trackTime = simpleTrack.trackTime,
            previewUrl = simpleTrack.previewUrl,
            artworkUrl100 = simpleTrack.artworkUrl100,
            collectionName = simpleTrack.collectionName,
            releaseYear = simpleTrack.releaseYear,
            country = simpleTrack.country,
            genre = simpleTrack.genre
        )
    }
}