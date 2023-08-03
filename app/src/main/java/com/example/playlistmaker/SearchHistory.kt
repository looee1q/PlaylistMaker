package com.example.playlistmaker

import android.content.SharedPreferences
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun writeTrackListToSharedPreferences(trackList: MutableList<Track>) {
        sharedPreferences.edit()
            .putString(HISTORY_TRACK_LIST_KEY, Json.encodeToString(trackList))
            .apply()
    }

    fun getTrackListFromSharedPreferences(): MutableList<Track> {
        val json = sharedPreferences.getString(HISTORY_TRACK_LIST_KEY, null) ?: return emptyArray<Track>().toMutableList()
        return Json.decodeFromString<ArrayList<Track>>(json)
    }

    companion object {
        private const val HISTORY_TRACK_LIST_KEY = "HISTORY_TRACK_LIST_KEY"
    }
}