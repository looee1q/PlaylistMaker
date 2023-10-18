package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.PlayerState

class MediaPlayerImpl() : PlayerRepository {

    private var state = PlayerState.DEFAULT
    private var mediaPlayer: MediaPlayer? = null

    override fun preparePlayer(track: Track, setPlayerState: () -> Unit) {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.apply {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                setPlayerState.invoke()
                state = PlayerState.PREPARED
            }
        }
    }

    override fun setOnCompletionListener(setPlayerState: () -> Unit) {
        mediaPlayer?.setOnCompletionListener {
            setPlayerState.invoke()
            state = PlayerState.PREPARED
        }
    }

    override fun play() {
        mediaPlayer?.start()
        state = PlayerState.PLAYING
    }

    override fun pause() {
        if (state == PlayerState.PLAYING) {
            mediaPlayer?.pause()
            state = PlayerState.PAUSED
        }
    }

    override fun destroyPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        state = PlayerState.DEFAULT
    }

    override fun getCurrentTime() = if (state == PlayerState.DEFAULT) {
        initialPositionInMillis
    } else {
        mediaPlayer!!.currentPosition
    }

    override fun getPlayerState() = state

    companion object {
        private const val initialPositionInMillis = 0
    }

}