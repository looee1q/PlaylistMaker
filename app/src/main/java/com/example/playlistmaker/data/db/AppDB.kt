package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.FavoriteTracksDAO
import com.example.playlistmaker.data.db.dao.PlaylistsDAO
import com.example.playlistmaker.data.db.dao.PlaylistsTracksDAO
import com.example.playlistmaker.data.db.entities.PlaylistEntity
import com.example.playlistmaker.data.db.entities.PlaylistTrackEntity
import com.example.playlistmaker.data.db.entities.TrackEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class])
abstract class AppDB : RoomDatabase() {

    abstract fun favoriteTracksDAO(): FavoriteTracksDAO

    abstract fun playlistsDAO(): PlaylistsDAO

    abstract fun playlistsTracksDAO(): PlaylistsTracksDAO
}