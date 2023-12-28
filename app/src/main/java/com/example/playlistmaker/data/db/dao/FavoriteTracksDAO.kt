package com.example.playlistmaker.data.db.dao

import androidx.room.*
import com.example.playlistmaker.data.db.entities.TrackEntity

@Dao
interface FavoriteTracksDAO {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun addTrackToDB(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    fun removeTrackFromDB(track: TrackEntity)

    @Query("SELECT * FROM favorite_tracks")
    fun showFavoriteTracks(): List<TrackEntity>

    @Query("SELECT trackId FROM favorite_tracks")
    fun showFavoriteTracksIDs(): List<String>
}