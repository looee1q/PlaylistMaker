package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.ui.mapper.Mapper
import com.example.playlistmaker.ui.models.TrackActivity

class PlayerViewModelFactory(track: TrackActivity) : ViewModelProvider.Factory {

    private val trackFactory by lazy { Creator.getFactoryForTrack(Mapper.mapTrackActivityToTrack(track)) }

    private val preparePlayerUseCase by lazy { trackFactory.providePreparePlayerUseCase() }
    private val setOnCompletionListenerUseCase by lazy { trackFactory.provideSetOnCompletionListenerUseCase() }
    private val pauseTrackUseCase by lazy { trackFactory.providePauseTrackUseCase() }
    private val playTrackUseCase by lazy { trackFactory.providePlayTrackUseCase() }
    private val playbackControlUseCase by lazy { trackFactory.providePlaybackControlUseCase() }
    private val getPlayingTrackTimeUseCase by lazy { trackFactory.provideGetPlayingTrackTimeUseCase() }
    private val getPlayerStateUseCase by lazy { trackFactory.provideGetPlayerStateUseCase() }
    private val destroyPlayerUseCase by lazy { trackFactory.provideDestroyPlayerUseCase() }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayerViewModel(
            preparePlayerUseCase = preparePlayerUseCase,
            setOnCompletionListenerUseCase = setOnCompletionListenerUseCase,
            pauseTrackUseCase = pauseTrackUseCase,
            playTrackUseCase = playTrackUseCase,
            playbackControlUseCase = playbackControlUseCase,
            getPlayingTrackTimeUseCase = getPlayingTrackTimeUseCase,
            getPlayerStateUseCase = getPlayerStateUseCase,
            destroyPlayerUseCase = destroyPlayerUseCase
        ) as T
    }
}