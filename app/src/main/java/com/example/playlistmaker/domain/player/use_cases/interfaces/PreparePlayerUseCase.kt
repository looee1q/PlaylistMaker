package com.example.playlistmaker.domain.player.use_cases.interfaces

interface PreparePlayerUseCase {
    fun execute(setPlayerState: () -> Unit)
}