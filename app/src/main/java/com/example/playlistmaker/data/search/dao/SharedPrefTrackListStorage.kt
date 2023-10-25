package com.example.playlistmaker.data.search.dao

import android.content.SharedPreferences
import com.example.playlistmaker.data.search.dto.SimpleTrack
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SharedPrefTrackListStorage(private val sharedPreferences: SharedPreferences) : TrackListStorage {

    override fun saveTrackList(trackList: List<SimpleTrack>) {
        sharedPreferences.edit()
            .putString(HISTORY_TRACK_LIST_KEY, Json.encodeToString(trackList))
            .apply()
    }

    override fun getTrackList(): List<SimpleTrack> {
        val json = sharedPreferences.getString(HISTORY_TRACK_LIST_KEY, null) ?: return emptyList<SimpleTrack>()
        return Json.decodeFromString<List<SimpleTrack>>(json)
    }

    companion object {
        const val HISTORY_OF_TRACKS = "HISTORY_OF_TRACKS"
        private const val HISTORY_TRACK_LIST_KEY = "HISTORY_TRACK_LIST_KEY"
    }
}