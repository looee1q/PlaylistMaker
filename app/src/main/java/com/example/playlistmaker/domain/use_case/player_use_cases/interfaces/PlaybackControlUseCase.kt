package com.example.playlistmaker.domain.use_case.player_use_cases.interfaces

import com.example.playlistmaker.domain.player.PlayerState

interface PlaybackControlUseCase {
    fun execute(doSmthWhilePlaying : () -> Unit, doSmthWhileOnPause : () -> Unit) : PlayerState
}