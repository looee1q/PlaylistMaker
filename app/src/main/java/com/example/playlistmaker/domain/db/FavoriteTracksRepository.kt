package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {

    suspend fun addTrackToDB(track: Track)

    suspend fun removeTrackFromDB(track: Track)

    fun showFavoriteTracks() : Flow<List<Track>>

    fun showFavoriteTracksIDs() : Flow<List<Long>>
}