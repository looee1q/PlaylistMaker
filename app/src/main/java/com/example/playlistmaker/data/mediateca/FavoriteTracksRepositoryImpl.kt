package com.example.playlistmaker.data.mediateca

import com.example.playlistmaker.data.db.AppDB
import com.example.playlistmaker.data.db.DBConvertor
import com.example.playlistmaker.domain.mediateca.favorites.FavoriteTracksRepository
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(private val appDB: AppDB) :
    FavoriteTracksRepository {
    override suspend fun addTrackToFavorites(track: Track) {
        appDB.favoriteTracksDAO().addTrackToDB(
            DBConvertor.convertTrackToTrackEntity(track)
        )
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        appDB.favoriteTracksDAO().removeTrackFromDB(
            DBConvertor.convertTrackToTrackEntity(track)
        )
    }

    override fun showFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDB.favoriteTracksDAO().showFavoriteTracks().map {
            DBConvertor.convertTrackEntityToTrack(it)
        }
        emit(tracks)
    }

    override fun showFavoriteTracksIDs(): Flow<List<Long>> = flow {
        val tracksIds = appDB.favoriteTracksDAO().showFavoriteTracksIDs().map {
            it.toLong()
        }
        emit(tracksIds)
    }
}