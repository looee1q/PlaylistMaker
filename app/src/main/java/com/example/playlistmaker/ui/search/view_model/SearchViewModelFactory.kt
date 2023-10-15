package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator

class SearchViewModelFactory : ViewModelProvider.Factory {

    private val writeHistoryTrackListToStorageUseCase by lazy {
        Creator.provideWriteHistoryTrackListToStorageUseCase()
    }
    private val getHistoryTrackListFromStorageUseCase by lazy {
        Creator.provideGetHistoryTrackListFromStorageUseCase()
    }

    private val getTracksByApiRequestUseCase by lazy {
        Creator.provideGetTracksByApiRequestUseCase()
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            writeHistoryTrackListToStorageUseCase = writeHistoryTrackListToStorageUseCase,
            getHistoryTrackListFromStorageUseCase = getHistoryTrackListFromStorageUseCase,
            getTracksByApiRequestUseCase = getTracksByApiRequestUseCase
        ) as T
    }
}