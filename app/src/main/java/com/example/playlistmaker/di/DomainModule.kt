package com.example.playlistmaker.di

import com.example.playlistmaker.domain.mediateca.favorites.use_cases.implementations.AddTrackToFavoritesUseCaseImpl
import com.example.playlistmaker.domain.mediateca.favorites.use_cases.implementations.GetFavoritesIDsUseCaseImpl
import com.example.playlistmaker.domain.mediateca.favorites.use_cases.implementations.RemoveTrackFromFavoritesUseCaseImpl
import com.example.playlistmaker.domain.mediateca.favorites.use_cases.implementations.ShowAllFavoritesUseCaseImpl
import com.example.playlistmaker.domain.mediateca.favorites.use_cases.interfaces.AddTrackToFavoritesUseCase
import com.example.playlistmaker.domain.mediateca.favorites.use_cases.interfaces.GetFavoritesIDsUseCase
import com.example.playlistmaker.domain.mediateca.favorites.use_cases.interfaces.RemoveTrackFromFavoritesUseCase
import com.example.playlistmaker.domain.mediateca.favorites.use_cases.interfaces.ShowAllFavoritesUseCase
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.implementations.*
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.*
import com.example.playlistmaker.domain.player.use_cases.implementations.DestroyPlayerUseCaseImpl
import com.example.playlistmaker.domain.player.use_cases.implementations.GetPlayerStateUseCaseImpl
import com.example.playlistmaker.domain.player.use_cases.implementations.GetPlayingTrackTimeUseCaseImpl
import com.example.playlistmaker.domain.player.use_cases.implementations.PauseTrackUseCaseImpl
import com.example.playlistmaker.domain.player.use_cases.implementations.PlaybackControlUseCaseImpl
import com.example.playlistmaker.domain.player.use_cases.implementations.PlayTrackUseCaseImpl
import com.example.playlistmaker.domain.player.use_cases.implementations.PreparePlayerUseCaseImpl
import com.example.playlistmaker.domain.player.use_cases.implementations.SetOnCompletionListenerUseCaseImpl
import com.example.playlistmaker.domain.player.use_cases.interfaces.DestroyPlayerUseCase
import com.example.playlistmaker.domain.player.use_cases.interfaces.GetPlayerStateUseCase
import com.example.playlistmaker.domain.player.use_cases.interfaces.GetPlayingTrackTimeUseCase
import com.example.playlistmaker.domain.player.use_cases.interfaces.PauseTrackUseCase
import com.example.playlistmaker.domain.player.use_cases.interfaces.PlaybackControlUseCase
import com.example.playlistmaker.domain.player.use_cases.interfaces.PlayTrackUseCase
import com.example.playlistmaker.domain.player.use_cases.interfaces.PreparePlayerUseCase
import com.example.playlistmaker.domain.player.use_cases.interfaces.SetOnCompletionListenerUseCase
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
import com.example.playlistmaker.domain.search.use_cases.implementations.GetTracksByApiRequestUseCaseImpl
import com.example.playlistmaker.domain.search.use_cases.implementations.WriteHistoryTrackListToStorageUseCaseImpl
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

    // modules for player

    factory<DestroyPlayerUseCase> {
        DestroyPlayerUseCaseImpl(get())
    }

    factory<GetPlayerStateUseCase> {
        GetPlayerStateUseCaseImpl(get())
    }

    factory<GetPlayingTrackTimeUseCase> {
        GetPlayingTrackTimeUseCaseImpl(get())
    }

    factory<PauseTrackUseCase> {
        PauseTrackUseCaseImpl(get())
    }

    factory<PlaybackControlUseCase> {
        PlaybackControlUseCaseImpl(get())
    }

    factory<PlayTrackUseCase> {
        PlayTrackUseCaseImpl(get())
    }

    factory<PreparePlayerUseCase> {
        PreparePlayerUseCaseImpl(get())
    }

    factory<SetOnCompletionListenerUseCase> {
        SetOnCompletionListenerUseCaseImpl(get())
    }

    // modules for UseCases which are interacted with favorite tracks using DB

    factory<AddTrackToFavoritesUseCase> {
        AddTrackToFavoritesUseCaseImpl(get())
    }

    factory<RemoveTrackFromFavoritesUseCase> {
        RemoveTrackFromFavoritesUseCaseImpl(get())
    }

    factory<ShowAllFavoritesUseCase> {
        ShowAllFavoritesUseCaseImpl(get())
    }

    factory<GetFavoritesIDsUseCase> {
        GetFavoritesIDsUseCaseImpl(get())
    }

    // modules for UseCases which are interacted with playlists using DB

    factory<AddPlaylistUseCase> {
        AddPlaylistUseCaseImpl(get())
    }

    factory<DeletePlaylistUseCase> {
        DeletePlaylistUseCaseImpl(get())
    }

    factory<UpdatePlaylistUseCase> {
        UpdatePlaylistUseCaseImpl(get())
    }

    factory<ShowPlaylistsUseCase> {
        ShowPlaylistsUseCaseImpl(get())
    }

    factory<AddTrackToPlaylistUseCase> {
        AddTrackToPlaylistUseCaseImpl(get())
    }

    factory<RemoveTrackFromPlaylistUseCase> {
        RemoveTrackFromPlaylistUseCaseImpl(get())
    }

    factory<GetPlaylistByIdUseCase> {
        GetPlaylistByIdUseCaseImpl(get())
    }

    factory<GetAllTracksFromPlaylistUseCase> {
        GetAllTracksFromPlaylistUseCaseImpl(get())
    }

    //modules for other mediateca UseCases

    factory<SharePlaylistUseCase> {
        SharePlaylistUseCaseImpl(get())
    }


}
