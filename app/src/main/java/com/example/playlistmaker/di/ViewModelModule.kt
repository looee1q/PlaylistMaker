package com.example.playlistmaker.di

import com.example.playlistmaker.ui.mediateca.favorites.view_model.FavouritesViewModel
import com.example.playlistmaker.ui.mediateca.playlists.view_model.PlaylistCreatorViewModel
import com.example.playlistmaker.ui.mediateca.playlists.view_model.PlaylistViewModel
import com.example.playlistmaker.ui.mediateca.playlists.view_model.PlaylistsViewModel
import com.example.playlistmaker.ui.models.TrackRepresentation
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.settings.SettingsViewModel
import com.example.playlistmaker.ui.settings.view_model.sharing.SharingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    // viewModels for settingsActivity

    viewModel<SettingsViewModel> {
        SettingsViewModel(get(), get(), get())
    }

    viewModel<SharingViewModel> {
        SharingViewModel(get(), get(), get())
    }

    // viewModels for searchActivity

    viewModel<SearchViewModel> {
        SearchViewModel(get(), get(), get())
    }

    // viewModels for PlayerActivity

    viewModel<PlayerViewModel> { (track: TrackRepresentation) ->
        PlayerViewModel(
            track = track,
            preparePlayerUseCase = get(),
            setOnCompletionListenerUseCase = get(),
            pauseTrackUseCase = get(),
            playTrackUseCase = get(),
            playbackControlUseCase = get(),
            getPlayingTrackTimeUseCase = get(),
            getPlayerStateUseCase = get(),
            destroyPlayerUseCase = get(),
            addTrackToFavoritesUseCase = get(),
            removeTrackFromFavoritesUseCase = get(),
            getTracksIDsFromDBUseCase = get(),
            showPlaylistsUseCase = get(),
            addTrackToPlaylistUseCase = get(),
            updatePlaylistUseCase = get()
        )
    }

    //viewModels for MediatecaActivity

    viewModel<FavouritesViewModel> {
        FavouritesViewModel(
            showAllFavoritesUseCase = get()
        )
    }

    viewModel<PlaylistsViewModel> {
        PlaylistsViewModel(
            showPlaylistsUseCase = get()
        )
    }

    viewModel<PlaylistCreatorViewModel> {
        PlaylistCreatorViewModel(
            addPlaylistUseCase = get()
        )
    }

    viewModel<PlaylistViewModel>() { (playlistId: Int) ->
        PlaylistViewModel(
            playlistId = playlistId,
            getPlaylistByIdUseCase = get(),
            getAllTracksFromPlaylistUseCase = get()
        )
    }

}