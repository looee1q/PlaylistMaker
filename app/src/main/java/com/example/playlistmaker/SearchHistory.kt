package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.presentation.models.TrackActivity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun writeTrackListToSharedPreferences(trackList: MutableList<TrackActivity>) {
        sharedPreferences.edit()
            .putString(HISTORY_TRACK_LIST_KEY, Json.encodeToString(trackList))
            .apply()
    }

    fun getTrackListFromSharedPreferences(): MutableList<TrackActivity> {
        val json = sharedPreferences.getString(HISTORY_TRACK_LIST_KEY, null) ?: return emptyArray<TrackActivity>().toMutableList()
        return Json.decodeFromString<ArrayList<TrackActivity>>(json)
    }

    companion object {
        private const val HISTORY_TRACK_LIST_KEY = "HISTORY_TRACK_LIST_KEY"
    }
}