package com.example.playlistmaker.creator

import android.app.Application
import com.example.playlistmaker.data.search.dao.HistoryTrackListDAOImpl
import com.example.playlistmaker.data.search.dao.SharedPrefTrackListStorage
import com.example.playlistmaker.data.search.network.RetrofitMusicApi
import com.example.playlistmaker.data.settings.ThemeRepositoryImpl
import com.example.playlistmaker.data.settings.SharedPrefThemeStorage
import com.example.playlistmaker.domain.search.api.MusicApi
import com.example.playlistmaker.domain.search.dao.HistoryTrackListDAO
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.use_cases.implementations.GetHistoryTrackListFromStorageUseCaseImpl
import com.example.playlistmaker.domain.use_case.search_tracks_use_cases.implementations.GetTracksByApiRequestUseCaseImpl
import com.example.playlistmaker.domain.use_case.search_tracks_use_cases.implementations.WriteHistoryTrackListToStorageUseCaseImpl
import com.example.playlistmaker.domain.search.use_cases.interfaces.GetHistoryTrackListFromStorageUseCase
import com.example.playlistmaker.domain.search.use_cases.interfaces.GetTracksByApiRequestUseCase
import com.example.playlistmaker.domain.search.use_cases.interfaces.WriteHistoryTrackListToStorageUseCase
import com.example.playlistmaker.domain.settings.repository.ThemeRepository
import com.example.playlistmaker.domain.settings.use_cases.implementations.SaveThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.use_cases.implementations.GetLastSavedThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.use_cases.implementations.SetNewThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.use_cases.interfaces.SaveThemeUseCase
import com.example.playlistmaker.domain.settings.use_cases.interfaces.GetLastSavedThemeUseCase
import com.example.playlistmaker.domain.settings.use_cases.interfaces.SetNewThemeUseCase

object Creator {

    lateinit var application: Application

    fun registryApplication(application: Application) {
        this.application = application
    }

    fun getFactoryForTrack(track: Track): PlayerUseCaseFactory {
        return PlayerUseCaseFactory(track)
    }

    //UseCases экрана настроек SettingsActivity
    fun provideSaveThemeUseCase(): SaveThemeUseCase {
        return SaveThemeUseCaseImpl(provideThemeRepository())
    }

    fun provideSetNewThemeUseCase(): SetNewThemeUseCase {
        return SetNewThemeUseCaseImpl(provideThemeRepository())
    }

    fun provideGetLastSavedThemeUseCase(): GetLastSavedThemeUseCase {
        return GetLastSavedThemeUseCaseImpl(provideThemeRepository())
    }

    private fun provideThemeRepository(): ThemeRepository {
        return ThemeRepositoryImpl(SharedPrefThemeStorage(application))
    }

    //UseCases экрана поиска SearchActivity
    fun provideGetTracksByApiRequestUseCase(): GetTracksByApiRequestUseCase {
        return GetTracksByApiRequestUseCaseImpl(provideMusicApi())
    }

    fun provideGetHistoryTrackListFromStorageUseCase(): GetHistoryTrackListFromStorageUseCase {
        return GetHistoryTrackListFromStorageUseCaseImpl(provideHistoryTrackListDao())
    }

    fun provideWriteHistoryTrackListToStorageUseCase(): WriteHistoryTrackListToStorageUseCase {
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