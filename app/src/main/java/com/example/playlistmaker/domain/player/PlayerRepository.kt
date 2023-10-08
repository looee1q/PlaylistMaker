package com.example.playlistmaker.domain.player

interface PlayerRepository {
    fun play()
    fun pause()
    fun preparePlayer(setPlayerState: () -> Unit)

    fun setOnCompletionListener(setPlayerState: () -> Unit)
    fun destroyPlayer()
    fun getCurrentTime(): Int
    fun getPlayerState(): PlayerState
}