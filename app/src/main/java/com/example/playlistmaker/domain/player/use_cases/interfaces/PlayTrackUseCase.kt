package com.example.playlistmaker.domain.player.use_cases.interfaces

import com.example.playlistmaker.domain.player.PlayerState

interface PlayTrackUseCase {
    fun execute(doActionWhilePlaying: () -> Unit) : PlayerState
}