package com.example.playlistmaker.creator

import com.example.playlistmaker.data.player.MediaPlayerImpl
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.use_cases.implementations.DestroyPlayerUseCaseImpl
import com.example.playlistmaker.domain.player.use_cases.implementations.GetPlayerStateUseCaseImpl
import com.example.playlistmaker.domain.player.use_cases.implementations.GetPlayingTrackTimeUseCaseImpl
import com.example.playlistmaker.domain.player.use_cases.implementations.PauseTrackUseCaseImpl
import com.example.playlistmaker.domain.player.use_cases.implementations.PlaybackControlUseCaseImpl
import com.example.playlistmaker.domain.player.use_cases.implementations.PlayTrackUseCaseImpl
import com.example.playlistmaker.domain.player.use_cases.implementations.PreparePlayerUseCaseImpl
import com.example.playlistmaker.domain.player.use_cases.interfaces.DestroyPlayerUseCase
import com.example.playlistmaker.domain.player.use_cases.interfaces.GetPlayerStateUseCase
import com.example.playlistmaker.domain.player.use_cases.interfaces.GetPlayingTrackTimeUseCase
import com.example.playlistmaker.domain.player.use_cases.interfaces.PauseTrackUseCase
import com.example.playlistmaker.domain.player.use_cases.interfaces.PlaybackControlUseCase
import com.example.playlistmaker.domain.player.use_cases.interfaces.PlayTrackUseCase
import com.example.playlistmaker.domain.player.use_cases.interfaces.PreparePlayerUseCase

class PlayerUseCaseFactory(val track: Track) {

    private val mediaPlayer = MediaPlayerImpl(track)

    //UseCases экрана плеера TrackInfoActivity
    fun provideDestroyPlayerUseCase() : DestroyPlayerUseCase {
        return DestroyPlayerUseCaseImpl(mediaPlayer)
    }

    fun provideGetPlayerStateUseCase() : GetPlayerStateUseCase {
        return GetPlayerStateUseCaseImpl(mediaPlayer)
    }

    fun provideGetPlayingTrackTimeUseCase() : GetPlayingTrackTimeUseCase {
        return GetPlayingTrackTimeUseCaseImpl(mediaPlayer)
    }

    fun providePauseTrackUseCase() : PauseTrackUseCase {
        return PauseTrackUseCaseImpl(mediaPlayer)
    }

    fun providePlaybackControlUseCase() : PlaybackControlUseCase {
        return PlaybackControlUseCaseImpl(mediaPlayer)
    }

    fun providePlayTrackUseCase() : PlayTrackUseCase {
        return PlayTrackUseCaseImpl(mediaPlayer)
    }

    fun providePreparePlayerUseCase() : PreparePlayerUseCase {
        return PreparePlayerUseCaseImpl(mediaPlayer)
    }

}