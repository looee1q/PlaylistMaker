package com.example.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.player.PlayerState
import com.example.playlistmaker.domain.player.use_cases.interfaces.PreparePlayerUseCase
import com.example.playlistmaker.domain.player.use_cases.interfaces.SetOnCompletionListenerUseCase
import com.example.playlistmaker.domain.player.use_cases.interfaces.PauseTrackUseCase
import com.example.playlistmaker.domain.player.use_cases.interfaces.PlayTrackUseCase
import com.example.playlistmaker.domain.player.use_cases.interfaces.PlaybackControlUseCase
import com.example.playlistmaker.domain.player.use_cases.interfaces.GetPlayingTrackTimeUseCase
import com.example.playlistmaker.domain.player.use_cases.interfaces.GetPlayerStateUseCase
import com.example.playlistmaker.domain.player.use_cases.interfaces.DestroyPlayerUseCase
import com.example.playlistmaker.ui.mapper.Mapper
import com.example.playlistmaker.ui.models.TrackRepresentation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val track: TrackRepresentation,
    private val preparePlayerUseCase: PreparePlayerUseCase,
    private val setOnCompletionListenerUseCase: SetOnCompletionListenerUseCase,
    private val pauseTrackUseCase: PauseTrackUseCase,
    private val playTrackUseCase: PlayTrackUseCase,
    private val playbackControlUseCase: PlaybackControlUseCase,
    private val getPlayingTrackTimeUseCase: GetPlayingTrackTimeUseCase,
    private val getPlayerStateUseCase: GetPlayerStateUseCase,
    private val destroyPlayerUseCase: DestroyPlayerUseCase
) : ViewModel() {

    private val mutableLiveDataPlayerState = MutableLiveData<PlayerState>().also {
        it.value = getPlayerStateUseCase.execute()
    }
    val liveDataPlayerState: LiveData<PlayerState> = mutableLiveDataPlayerState

    private val mutableLiveDataTrackPlaybackProgress = MutableLiveData<Int>().also {
        it.value = getPlayingTrackTimeUseCase.execute()
    }
    val liveDataTrackPlaybackProgress: LiveData<Int> = mutableLiveDataTrackPlaybackProgress

    init {
        preparePlayerUseCase.execute(track = Mapper.mapTrackRepresentationToTrack(track)) {
            mutableLiveDataPlayerState.postValue(PlayerState.PREPARED)
        }
        setOnCompletionListenerUseCase.execute {
            mutableLiveDataPlayerState.postValue(PlayerState.PREPARED)
        }
    }

    override fun onCleared() {
        destroyPlayerUseCase.execute()
        super.onCleared()
    }

    fun pausePlayer() {
        mutableLiveDataPlayerState.postValue(pauseTrackUseCase.execute { })
    }

    fun playbackControl() {
        mutableLiveDataPlayerState.postValue(playbackControlUseCase.execute(
            doActionWhileOnPause = { }, doActionWhilePlaying = { }
        ))
    }

    fun startTimeUpdater() {
        Log.d("PlayerState","${getPlayerStateUseCase.execute()}")
        viewModelScope.launch {
            while (getPlayerStateUseCase.execute() == PlayerState.PLAYING) {
                delay(TRACK_TIME_UPDATE_FREQUENCY_MILLIS)
                mutableLiveDataTrackPlaybackProgress.value = getPlayingTrackTimeUseCase.execute()
                Log.d("CURRENT_TIME", "${mutableLiveDataTrackPlaybackProgress.value}")
            }
        }
    }

    companion object {
        private const val TRACK_TIME_UPDATE_FREQUENCY_MILLIS = 300L
    }

}