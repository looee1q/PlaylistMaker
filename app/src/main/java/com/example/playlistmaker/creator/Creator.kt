package com.example.playlistmaker.creator

import android.app.Application
import com.example.playlistmaker.data.dao.HistoryTrackListDAOImpl
import com.example.playlistmaker.data.dao.SharedPrefTrackListStorage
import com.example.playlistmaker.data.network.RetrofitMusicApi
import com.example.playlistmaker.domain.api.MusicApi
import com.example.playlistmaker.domain.dao.HistoryTrackListDAO
import com.example.playlistmaker.domain.model.Track
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

    fun getFactoryForTrack(track: Track) : PlayerUseCaseFactory {
        return PlayerUseCaseFactory(track)
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

    //data-имплементаторы интерфейсов domain слоя, используемых UseCase(ами) SearchActivity
    private fun provideMusicApi(): MusicApi {
        return RetrofitMusicApi()
    }

    private fun provideHistoryTrackListDao(): HistoryTrackListDAO {
        return HistoryTrackListDAOImpl(SharedPrefTrackListStorage(application))
    }
}