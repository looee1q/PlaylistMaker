package com.example.playlistmaker.data

interface UIInteractor {
    fun setPauseButtonIcon(drawableId: Int)
    fun setPlayButtonIcon(drawableId: Int)
    fun setTrackTimeWhilePlaying(timeMillis: Int)
}