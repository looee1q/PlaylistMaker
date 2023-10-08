package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.PlayerState

class MediaPlayerImpl(private val track: Track) : PlayerRepository {

    private var mediaPlayer = MediaPlayer()

    private var state = PlayerState.DEFAULT

    override fun preparePlayer(setPlayerState: () -> Unit) {
        mediaPlayer.apply {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                setPlayerState.invoke()
                state = PlayerState.PREPARED
            }
        }
    }

    override fun setOnCompletionListener(setPlayerState: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            setPlayerState.invoke()
            state = PlayerState.PREPARED
        }
    }

    override fun play() {
        mediaPlayer.start()
        state = PlayerState.PLAYING
    }

    override fun pause() {
        if (state == PlayerState.PLAYING) {
            mediaPlayer.pause()
            state = PlayerState.PAUSED
        }
    }

    override fun destroyPlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentTime() = mediaPlayer.currentPosition

    override fun getPlayerState() = state

}