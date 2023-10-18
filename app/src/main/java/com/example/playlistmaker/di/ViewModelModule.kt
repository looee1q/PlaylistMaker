package com.example.playlistmaker.di

import com.example.playlistmaker.ui.models.TrackActivity
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

    viewModel<PlayerViewModel> { (track: TrackActivity) ->
        PlayerViewModel(
            track = track,
            preparePlayerUseCase = get(),
            setOnCompletionListenerUseCase = get(),
            pauseTrackUseCase = get(),
            playTrackUseCase = get(),
            playbackControlUseCase = get(),
            getPlayingTrackTimeUseCase = get(),
            getPlayerStateUseCase = get(),
            destroyPlayerUseCase = get()
        )
    }

}