package com.example.playlistmaker.data

interface Player {

    var state: PlayerCState

    fun preparePlayer()
    fun start()
    fun pause()
    fun playbackControl()
    fun destroy()
    fun getCurrentPosition() : Int
    fun updateTrackTime()
}