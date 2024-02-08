package com.example.playlistmaker.data.db.dao

import androidx.room.*
import com.example.playlistmaker.data.db.entities.PlaylistTrackEntity
import com.example.playlistmaker.data.db.entities.TrackEntity

@Dao
interface PlaylistsTracksDAO {

    @Insert(entity = PlaylistTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun addTrackToDB(track: PlaylistTrackEntity)

    @Delete(entity = PlaylistTrackEntity::class)
    fun removeTrackFromDB(track: PlaylistTrackEntity)
    @Query("DELETE FROM playlists_tracks WHERE trackId = :trackId")
    fun removeTrackFromDB(trackId: Long)

    @Query("SELECT * FROM playlists_tracks")
    fun getAllPlaylistsTracks(): List<TrackEntity>
}