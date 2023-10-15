package com.example.playlistmaker.di

import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.settings.SettingsViewModel
import com.example.playlistmaker.ui.settings.view_model.sharing.SharingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    // viewModules for settingsActivity

    viewModel<SettingsViewModel> {
        SettingsViewModel(get(), get(), get())
    }

    viewModel<SharingViewModel> {
        SharingViewModel(get(), get(), get())
    }

    // viewModules for searchActivity

    viewModel<SearchViewModel> {
        SearchViewModel(get(), get(), get())
    }

}