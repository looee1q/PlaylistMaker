package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.dao.HistoryTrackListDAOImpl
import com.example.playlistmaker.data.dao.SharedPrefTrackListStorage
import com.example.playlistmaker.data.network.RetrofitMusicApi
import com.example.playlistmaker.data.player.MediaPlayerImpl
import com.example.playlistmaker.domain.api.MusicApi
import com.example.playlistmaker.domain.dao.HistoryTrackListDAO
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.use_case.search_tracks_use_cases.implementations.*
import com.example.playlistmaker.domain.use_case.search_tracks_use_cases.interfaces.*
import com.example.playlistmaker.domain.use_case.player_use_cases.implementations.*
import com.example.playlistmaker.domain.use_case.player_use_cases.interfaces.*

object Creator {

    //UseCases экрана поиска SearchActivity
    fun provideGetTracksByApiRequestUseCase() : GetTracksByApiRequestUseCase {
        return GetTracksByApiRequestUseCaseImpl(provideMusicApi())
    }

    fun provideGetHistoryTrackListFromStorageUseCase(context: Context) : GetHistoryTrackListFromStorageUseCase {
        return GetHistoryTrackListFromStorageUseCaseImpl(provideHistoryTrackListDao(context))
    }

    fun provideWriteHistoryTrackListToStorageUseCase(context: Context) : WriteHistoryTrackListToStorageUseCase {
        return WriteHistoryTrackListToStorageUseCaseImpl(provideHistoryTrackListDao(context))
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

    private fun provideHistoryTrackListDao(context: Context): HistoryTrackListDAO {
        return HistoryTrackListDAOImpl(SharedPrefTrackListStorage(context))
    }

    //data-имплементатор интерфейса domain слоя, используемых UseCase(ами) TrackInfoActivity
    fun provideMediaPlayerImpl(track: Track) : MediaPlayerImpl {
        return MediaPlayerImpl(track)
    }
}