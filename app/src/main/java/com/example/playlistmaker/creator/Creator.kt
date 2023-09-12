package com.example.playlistmaker.creator

import com.example.playlistmaker.data.network.RetrofitMusicApi
import com.example.playlistmaker.domain.api.MusicApi
import com.example.playlistmaker.domain.use_case.GetTracksByApiRequestUseCase

object Creator {

    fun provideGetTracksByApiRequestUseCase() : GetTracksByApiRequestUseCase {
        return GetTracksByApiRequestUseCase(provideMusicApi())
    }

    private fun provideMusicApi(): MusicApi {
        return RetrofitMusicApi()
    }
}