package com.example.playlistmaker

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class Track(val trackName: String,
                val artistName: String,
                @SerializedName("trackTimeMillis") var trackTime: String,
                val artworkUrl100: String) {

    companion object {
        fun convertTrackTimeMillisToTrackTime(tracks: List<Track>) {
            for (track in tracks) {
                track.trackTime =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime.toLong())
            }
        }
    }
}