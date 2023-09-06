package com.example.playlistmaker.data

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.R
import com.example.playlistmaker.Track

class MediaPlayerC(val track: Track, val uiInteractor: UIInteractor, val androidInteractor: AndroidInteractor) : Player {

    private var mediaPlayer = MediaPlayer()
    override var state = PlayerCState.DEFAULT

    private val trackTimeUpdater = TrackTimeUpdater(mediaPlayer = this, uiInteractor = uiInteractor, handler = androidInteractor.getMainThreadHandler())

    override fun preparePlayer() {
        mediaPlayer.apply {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                state = PlayerCState.PREPARED
            }
            setOnCompletionListener {
                state = PlayerCState.PREPARED
            }
        }
    }

    override fun start() {
        mediaPlayer.start()
        Log.d("PLAYER_STATE", "Начато (продолжено) воспроизведение трека")
        uiInteractor.setPauseButtonIcon(R.drawable.pause_button_icon)
        state = PlayerCState.PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        Log.d("PLAYER_STATE", "Воспроизведение трека приостановлено")
        uiInteractor.setPlayButtonIcon(R.drawable.play_button_icon)
        state = PlayerCState.PAUSED
    }

    override fun playbackControl() {
        when (state) {
            PlayerCState.PLAYING -> pause()
            PlayerCState.PAUSED, PlayerCState.PREPARED -> start()
            PlayerCState.DEFAULT -> {}
        }
    }

    override fun destroy() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition() = mediaPlayer.currentPosition

    override fun updateTrackTime() {
        trackTimeUpdater.updateTrackTime()
    }

}