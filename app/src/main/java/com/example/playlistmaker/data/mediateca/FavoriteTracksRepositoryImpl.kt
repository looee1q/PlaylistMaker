package com.example.playlistmaker.data.mediateca

import com.example.playlistmaker.data.db.FavoriteTracksDB
import com.example.playlistmaker.data.db.TrackDBConvertor
import com.example.playlistmaker.domain.mediateca.favorites.FavoriteTracksRepository
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(private val favoriteTracksDB: FavoriteTracksDB) :
    FavoriteTracksRepository {
    override suspend fun addTrackToFavorites(track: Track) {
        favoriteTracksDB.favoriteTracksDAO().addTrackToDB(
            TrackDBConvertor.convertTrackToTrackEntity(track)
        )
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        favoriteTracksDB.favoriteTracksDAO().removeTrackFromDB(
            TrackDBConvertor.convertTrackToTrackEntity(track)
        )
    }

    override fun showFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = favoriteTracksDB.favoriteTracksDAO().showFavoriteTracks().map {
            TrackDBConvertor.convertTrackEntityToTrack(it)
        }
        emit(tracks)
    }

    override fun showFavoriteTracksIDs(): Flow<List<Long>> = flow {
        val tracksIds = favoriteTracksDB.favoriteTracksDAO().showFavoriteTracksIDs().map {
            it.toLong()
        }
        emit(tracksIds)
    }
}