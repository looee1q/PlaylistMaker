package com.example.playlistmaker.data

import android.os.Handler
import android.util.Log
import com.example.playlistmaker.R

class TrackTimeUpdater(val mediaPlayer: Player, val uiInteractor: UIInteractor, val handler: Handler) {

    private var currentTimePlayingMillis = 0

    fun updateTrackTime() {
        val updateTrackTimeRunnable = object : Runnable {
            override fun run() {
                currentTimePlayingMillis = mediaPlayer.getCurrentPosition()
                Log.d("CURRENT_TIME", "$currentTimePlayingMillis")
                uiInteractor.setTrackTimeWhilePlaying(currentTimePlayingMillis)
                when (mediaPlayer.state) {
                    PlayerCState.PLAYING -> handler.postDelayed(this, TRACK_TIME_UPDATE_FREQUENCY_MILLIS)
                    PlayerCState.PAUSED -> handler.removeCallbacks(this)
                    PlayerCState.PREPARED -> {
                        handler.removeCallbacks(this)
                        uiInteractor.setTrackTimeWhilePlaying(0)
                        uiInteractor.setPauseButtonIcon(R.drawable.play_button_icon)
                    }
                    PlayerCState.DEFAULT -> {}
                }
            }
        }
        handler.post(updateTrackTimeRunnable)
    }

    companion object {
        private const val TRACK_TIME_UPDATE_FREQUENCY_MILLIS = 500L
    }
}