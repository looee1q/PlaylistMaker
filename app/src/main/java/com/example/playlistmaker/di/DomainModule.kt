package com.example.playlistmaker.di

import com.example.playlistmaker.domain.search.use_cases.implementations.GetHistoryTrackListFromStorageUseCaseImpl
import com.example.playlistmaker.domain.search.use_cases.interfaces.GetHistoryTrackListFromStorageUseCase
import com.example.playlistmaker.domain.search.use_cases.interfaces.GetTracksByApiRequestUseCase
import com.example.playlistmaker.domain.search.use_cases.interfaces.WriteHistoryTrackListToStorageUseCase
import com.example.playlistmaker.domain.settings.use_cases.implementations.GetLastSavedThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.use_cases.implementations.SaveThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.use_cases.implementations.SetNewThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.use_cases.interfaces.GetLastSavedThemeUseCase
import com.example.playlistmaker.domain.settings.use_cases.interfaces.SaveThemeUseCase
import com.example.playlistmaker.domain.settings.use_cases.interfaces.SetNewThemeUseCase
import com.example.playlistmaker.domain.sharing.use_cases.implementations.OpenTermsUseCaseImpl
import com.example.playlistmaker.domain.sharing.use_cases.implementations.ShareAppUseCaseImpl
import com.example.playlistmaker.domain.sharing.use_cases.implementations.WriteToSupportUseCaseImpl
import com.example.playlistmaker.domain.sharing.use_cases.interfaces.OpenTermsUseCase
import com.example.playlistmaker.domain.sharing.use_cases.interfaces.ShareAppUseCase
import com.example.playlistmaker.domain.sharing.use_cases.interfaces.WriteToSupportUseCase
import com.example.playlistmaker.domain.use_case.search_tracks_use_cases.implementations.GetTracksByApiRequestUseCaseImpl
import com.example.playlistmaker.domain.use_case.search_tracks_use_cases.implementations.WriteHistoryTrackListToStorageUseCaseImpl
import org.koin.dsl.module

val domainModule = module {

    // modules for settings and sharing
    single<SaveThemeUseCase> {
        SaveThemeUseCaseImpl(get())
    }

    single<SetNewThemeUseCase> {
        SetNewThemeUseCaseImpl(get())
    }

    single<GetLastSavedThemeUseCase> {
        GetLastSavedThemeUseCaseImpl(get())
    }

    single<ShareAppUseCase> {
        ShareAppUseCaseImpl(get())
    }

    single<WriteToSupportUseCase> {
        WriteToSupportUseCaseImpl(get())
    }

    single<OpenTermsUseCase> {
        OpenTermsUseCaseImpl(get())
    }

    // modules for search

    single<GetTracksByApiRequestUseCase> {
        GetTracksByApiRequestUseCaseImpl(get())
    }

    single<GetHistoryTrackListFromStorageUseCase> {
        GetHistoryTrackListFromStorageUseCaseImpl(get())
    }

    single<WriteHistoryTrackListToStorageUseCase> {
        WriteHistoryTrackListToStorageUseCaseImpl(get())
    }

}
