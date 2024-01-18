package com.example.playlistmaker.domain.mediateca.favorites

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {

    suspend fun addTrackToFavorites(track: Track)

    suspend fun removeTrackFromFavorites(track: Track)

    fun showFavoriteTracks() : Flow<List<Track>>

    fun showFavoriteTracksIDs() : Flow<List<Long>>
}