package com.example.playlistmaker.domain.use_case.player_use_cases.interfaces

import com.example.playlistmaker.domain.player.PlayerState

interface PreparePlayerUseCase {
    fun execute() : PlayerState
}