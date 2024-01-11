package com.example.playlistmaker.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_playlists")
data class PlaylistEntity(
    val title: String,
    val description: String,
    val coverUri: String,
    val tracksId: String,
    val size: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
