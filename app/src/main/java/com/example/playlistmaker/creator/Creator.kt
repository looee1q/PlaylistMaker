package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import com.example.playlistmaker.data.dao.HistoryTrackListDAOImpl
import com.example.playlistmaker.data.dao.SharedPrefTrackListStorage
import com.example.playlistmaker.data.network.RetrofitMusicApi
import com.example.playlistmaker.data.player.MediaPlayerImpl
import com.example.playlistmaker.domain.api.MusicApi
import com.example.playlistmaker.domain.dao.HistoryTrackListDAO
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.use_case.player_use_cases.implementations.DestroyPlayerUseCaseImpl
import com.example.playlistmaker.domain.use_case.player_use_cases.implementations.GetPlayerStateUseCaseImpl
import com.example.playlistmaker.domain.use_case.player_use_cases.implementations.GetPlayingTrackTimeUseCaseImpl
import com.example.playlistmaker.domain.use_case.player_use_cases.implementations.PauseTrackUseCaseImpl
import com.example.playlistmaker.domain.use_case.player_use_cases.implementations.PlaybackControlUseCaseImpl
import com.example.playlistmaker.domain.use_case.player_use_cases.implementations.PlayTrackUseCaseImpl
import com.example.playlistmaker.domain.use_case.player_use_cases.implementations.PreparePlayerUseCaseImpl
import com.example.playlistmaker.domain.use_case.player_use_cases.interfaces.DestroyPlayerUseCase
import com.example.playlistmaker.domain.use_case.player_use_cases.interfaces.GetPlayerStateUseCase
import com.example.playlistmaker.domain.use_case.player_use_cases.interfaces.GetPlayingTrackTimeUseCase
import com.example.playlistmaker.domain.use_case.player_use_cases.interfaces.PauseTrackUseCase
import com.example.playlistmaker.domain.use_case.player_use_cases.interfaces.PlaybackControlUseCase
import com.example.playlistmaker.domain.use_case.player_use_cases.interfaces.PlayTrackUseCase
import com.example.playlistmaker.domain.use_case.player_use_cases.interfaces.PreparePlayerUseCase
import com.example.playlistmaker.domain.use_case.search_tracks_use_cases.implementations.GetHistoryTrackListFromStorageUseCaseImpl
import com.example.playlistmaker.domain.use_case.search_tracks_use_cases.implementations.GetTracksByApiRequestUseCaseImpl
import com.example.playlistmaker.domain.use_case.search_tracks_use_cases.implementations.WriteHistoryTrackListToStorageUseCaseImpl
import com.example.playlistmaker.domain.use_case.search_tracks_use_cases.interfaces.GetHistoryTrackListFromStorageUseCase
import com.example.playlistmaker.domain.use_case.search_tracks_use_cases.interfaces.GetTracksByApiRequestUseCase
import com.example.playlistmaker.domain.use_case.search_tracks_use_cases.interfaces.WriteHistoryTrackListToStorageUseCase

object Creator {

    lateinit var application: Application

    fun registryApplication(application: Application) {
        this.application = application
    }

    //UseCases экрана поиска SearchActivity
    fun provideGetTracksByApiRequestUseCase() : GetTracksByApiRequestUseCase {
        return GetTracksByApiRequestUseCaseImpl(provideMusicApi())
    }

    fun provideGetHistoryTrackListFromStorageUseCase() : GetHistoryTrackListFromStorageUseCase {
        return GetHistoryTrackListFromStorageUseCaseImpl(provideHistoryTrackListDao())
    }

    fun provideWriteHistoryTrackListToStorageUseCase() : WriteHistoryTrackListToStorageUseCase {
        return WriteHistoryTrackListToStorageUseCaseImpl(provideHistoryTrackListDao())
    }

    //UseCases экрана плеера TrackInfoActivity
    fun provideDestroyPlayerUseCase(player: PlayerRepository) : DestroyPlayerUseCase {
        return DestroyPlayerUseCaseImpl(player)
    }

    fun provideGetPlayerStateUseCase(player: PlayerRepository) : GetPlayerStateUseCase {
        return GetPlayerStateUseCaseImpl(player)
    }

    fun provideGetPlayingTrackTimeUseCase(player: PlayerRepository) : GetPlayingTrackTimeUseCase {
        return GetPlayingTrackTimeUseCaseImpl(player)
    }

    fun providePauseTrackUseCase(player: PlayerRepository) : PauseTrackUseCase {
        return PauseTrackUseCaseImpl(player)
    }

    fun providePlaybackControlUseCase(player: PlayerRepository) : PlaybackControlUseCase {
        return PlaybackControlUseCaseImpl(player)
    }

    fun providePlayTrackUseCase(player: PlayerRepository) : PlayTrackUseCase {
        return PlayTrackUseCaseImpl(player)
    }

    fun providePreparePlayerUseCase(player: PlayerRepository) : PreparePlayerUseCase {
        return PreparePlayerUseCaseImpl(player)
    }

    //data-имплементаторы интерфейсов domain слоя, используемых UseCase(ами) SearchActivity
    private fun provideMusicApi(): MusicApi {
        return RetrofitMusicApi()
    }

    private fun provideHistoryTrackListDao(): HistoryTrackListDAO {
        return HistoryTrackListDAOImpl(SharedPrefTrackListStorage(application))
    }

    //data-имплементатор интерфейса domain слоя, используемых UseCase(ами) TrackInfoActivity
    fun provideMediaPlayerImpl(track: Track) : MediaPlayerImpl {
        return MediaPlayerImpl(track)
    }
}