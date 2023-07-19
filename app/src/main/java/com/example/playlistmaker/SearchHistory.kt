package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(val sharedPreferences: SharedPreferences) {

    private val gson = Gson()

    fun writeTrackListToSharedPreferences(trackList: MutableList<Track>) {
        sharedPreferences.edit()
            .putString(HISTORY_TRACK_LIST_KEY, gson.toJson(trackList))
            .apply()
    }

    fun getTrackListFromSharedPreferences(): MutableList<Track> {
        val json = sharedPreferences.getString(HISTORY_TRACK_LIST_KEY, null) ?: return emptyArray<Track>().toMutableList()
        return gson.fromJson(json, Array<Track>::class.java).toMutableList()
    }

    companion object {
        private const val HISTORY_TRACK_LIST_KEY = "HISTORY_TRACK_LIST_KEY"
    }
}