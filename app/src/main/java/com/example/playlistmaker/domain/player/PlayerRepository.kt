package com.example.playlistmaker.domain.player

interface PlayerRepository {
    fun play()
    fun pause()
    fun preparePlayer()
    fun destroyPlayer()
    fun getCurrentTime() : Int
    fun getPlayerState() : PlayerState
}