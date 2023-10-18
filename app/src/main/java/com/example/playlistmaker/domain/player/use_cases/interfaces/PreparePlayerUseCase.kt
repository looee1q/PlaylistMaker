package com.example.playlistmaker.domain.player.use_cases.interfaces

import com.example.playlistmaker.domain.model.Track

interface PreparePlayerUseCase {
    fun execute(track: Track, setPlayerState: () -> Unit)
}