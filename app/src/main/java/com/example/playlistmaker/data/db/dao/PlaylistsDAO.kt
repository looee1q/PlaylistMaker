package com.example.playlistmaker.data.db.dao

import androidx.room.*
import com.example.playlistmaker.data.db.entities.PlaylistEntity

@Dao
interface PlaylistsDAO {

    @Insert(entity = PlaylistEntity::class)
    fun addPlaylist(playlistEntity: PlaylistEntity)

    @Delete(entity = PlaylistEntity::class)
    fun deletePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM user_playlists")
    fun showPlaylists(): List<PlaylistEntity>

    @Update(entity = PlaylistEntity::class)
    fun updatePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM user_playlists WHERE id = :playlistId")
    fun getPlaylist(playlistId: Int) : PlaylistEntity
}