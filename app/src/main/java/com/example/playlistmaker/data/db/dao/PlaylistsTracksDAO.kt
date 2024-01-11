package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker.data.db.entities.PlaylistTrackEntity

@Dao
interface PlaylistsTracksDAO {

    @Insert(entity = PlaylistTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun addTrackToDB(track: PlaylistTrackEntity)

    @Delete(entity = PlaylistTrackEntity::class)
    fun removeTrackFromDB(track: PlaylistTrackEntity)
}