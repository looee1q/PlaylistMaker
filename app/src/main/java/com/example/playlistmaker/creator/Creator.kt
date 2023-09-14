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
import com.example.playlistmaker.domain.use_case.GetHistoryTrackListFromStorageUseCase
import com.example.playlistmaker.domain.use_case.GetTracksByApiRequestUseCase
import com.example.playlistmaker.domain.use_case.WriteHistoryTrackListToStorageUseCase
import com.example.playlistmaker.domain.use_case.player_use_cases.*

object Creator {

    //UseCases экрана поиска SearchActivity
    fun provideGetTracksByApiRequestUseCase() : GetTracksByApiRequestUseCase {
        return GetTracksByApiRequestUseCase(provideMusicApi())
    }

    fun provideGetHistoryTrackListFromStorageUseCase(context: Context) : GetHistoryTrackListFromStorageUseCase {
        return GetHistoryTrackListFromStorageUseCase(provideHistoryTrackListDao(context))
    }

    fun provideWriteHistoryTrackListToStorageUseCase(context: Context) : WriteHistoryTrackListToStorageUseCase {
        return WriteHistoryTrackListToStorageUseCase(provideHistoryTrackListDao(context))
    }

    //UseCases экрана плеера TrackInfoActivity
    fun provideDestroyPlayerUseCase(player: PlayerRepository) : DestroyPlayerUseCase {
        return DestroyPlayerUseCase(player)
    }

    fun provideGetPlayerStateUseCase(player: PlayerRepository) : GetPlayerStateUseCase {
        return GetPlayerStateUseCase(player)
    }

    fun provideGetPlayingTrackTimeUseCase(player: PlayerRepository) : GetPlayingTrackTimeUseCase {
        return GetPlayingTrackTimeUseCase(player)
    }

    fun providePauseTrackUseCase(player: PlayerRepository) : PauseTrackUseCase {
        return PauseTrackUseCase(player)
    }

    fun providePlaybackControlUseCase(player: PlayerRepository) : PlaybackControlUseCase {
        return PlaybackControlUseCase(player)
    }

    fun providePlayTrackUseCase(player: PlayerRepository) : PlayTrackUseCase {
        return PlayTrackUseCase(player)
    }

    fun providePreparePlayerUseCase(player: PlayerRepository) : PreparePlayerUseCase {
        return PreparePlayerUseCase(player)
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