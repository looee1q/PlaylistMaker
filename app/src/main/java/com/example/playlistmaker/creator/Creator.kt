package com.example.playlistmaker.creator

import android.app.Application
import com.example.playlistmaker.data.search.dao.HistoryTrackListDAOImpl
import com.example.playlistmaker.data.search.dao.SharedPrefTrackListStorage
import com.example.playlistmaker.data.search.network.RetrofitMusicApi
import com.example.playlistmaker.data.settings.ThemeRepositoryImpl
import com.example.playlistmaker.data.settings.SharedPrefThemeStorage
import com.example.playlistmaker.data.sharing.ExternalNavigatorImpl
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
import com.example.playlistmaker.domain.sharing.repository.ExternalNavigator
import com.example.playlistmaker.domain.sharing.use_cases.implementations.OpenTermsUseCaseImpl
import com.example.playlistmaker.domain.sharing.use_cases.implementations.ShareAppUseCaseImpl
import com.example.playlistmaker.domain.sharing.use_cases.implementations.WriteToSupportUseCaseImpl
import com.example.playlistmaker.domain.sharing.use_cases.interfaces.OpenTermsUseCase
import com.example.playlistmaker.domain.sharing.use_cases.interfaces.ShareAppUseCase
import com.example.playlistmaker.domain.sharing.use_cases.interfaces.WriteToSupportUseCase

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

    fun provideShareAppUseCase(): ShareAppUseCase {
        return ShareAppUseCaseImpl(provideExternalNavigator())
    }

    fun provideWriteToSupportUseCase(): WriteToSupportUseCase {
        return WriteToSupportUseCaseImpl(provideExternalNavigator())
    }

    fun provideOpenTermsUseCase(): OpenTermsUseCase {
        return OpenTermsUseCaseImpl(provideExternalNavigator())
    }

    private fun provideThemeRepository(): ThemeRepository {
        return ThemeRepositoryImpl(SharedPrefThemeStorage(application))
    }

    private fun provideExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(application)
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