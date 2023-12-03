package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [TrackEntity::class])
abstract class FavoriteTracksDB : RoomDatabase() {

    abstract fun favoriteTracksDAO(): FavoriteTracksDAO
}