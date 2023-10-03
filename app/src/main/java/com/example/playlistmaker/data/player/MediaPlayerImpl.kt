package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.data.dto.SimpleTrack
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.PlayerState
import okhttp3.internal.wait

class MediaPlayerImpl(val track: Track) : PlayerRepository {

    private var mediaPlayer = MediaPlayer()

    var state = PlayerState.DEFAULT

    override fun preparePlayer() {
        mediaPlayer.apply {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                state = PlayerState.PREPARED
                Log.d("InsidePreparedListener", "$state")
            }
            setOnCompletionListener {
                state = PlayerState.PREPARED
            }
            Log.d("PreparePlayerMediaPlayerImpl", "$state")
        }
    }

    override fun play() {
        mediaPlayer.start()
        state = PlayerState.PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        state = PlayerState.PAUSED
    }

    override fun destroyPlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentTime() = mediaPlayer.currentPosition

    override fun getPlayerState() = state

}