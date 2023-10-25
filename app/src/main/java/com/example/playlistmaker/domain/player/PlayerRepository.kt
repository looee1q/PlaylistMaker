package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.model.Track

interface PlayerRepository {
    fun play()
    fun pause()
    fun preparePlayer(track: Track, setPlayerState: () -> Unit)

    fun setOnCompletionListener(setPlayerState: () -> Unit)
    fun destroyPlayer()
    fun getCurrentTime(): Int
    fun getPlayerState(): PlayerState
}