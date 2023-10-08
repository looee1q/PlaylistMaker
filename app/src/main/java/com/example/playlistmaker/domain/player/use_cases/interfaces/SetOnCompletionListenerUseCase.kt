package com.example.playlistmaker.domain.player.use_cases.interfaces

interface SetOnCompletionListenerUseCase {
    fun execute(setPlayerState: () -> Unit)
}