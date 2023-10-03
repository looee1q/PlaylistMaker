package com.example.playlistmaker.domain.player.use_cases.interfaces

import com.example.playlistmaker.domain.player.PlayerState

interface GetPlayerStateUseCase {
    fun execute() : PlayerState
}