package com.example.playlistmaker.ui.player.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.mediateca.favorites.use_cases.interfaces.AddTrackToFavoritesUseCase
import com.example.playlistmaker.domain.mediateca.favorites.use_cases.interfaces.GetFavoritesIDsUseCase
import com.example.playlistmaker.domain.mediateca.favorites.use_cases.interfaces.RemoveTrackFromFavoritesUseCase
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.AddTrackToPlaylistsTracksStorageUseCase
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.ShowPlaylistsUseCase
import com.example.playlistmaker.domain.mediateca.playlists.use_cases.interfaces.UpdatePlaylistUseCase
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
import com.example.playlistmaker.ui.player.model.TrackPlaylistRelationship
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
    private val destroyPlayerUseCase: DestroyPlayerUseCase,
    private val addTrackToFavoritesUseCase: AddTrackToFavoritesUseCase,
    private val removeTrackFromFavoritesUseCase: RemoveTrackFromFavoritesUseCase,
    private val getTracksIDsFromDBUseCase: GetFavoritesIDsUseCase,
    private val showPlaylistsUseCase: ShowPlaylistsUseCase,
    private val addTrackToPlaylistsTracksStorage: AddTrackToPlaylistsTracksStorageUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase
) : ViewModel() {

    private val mutableLiveDataPlayerState = MutableLiveData<PlayerState>().also {
        it.value = getPlayerStateUseCase.execute()
    }
    val liveDataPlayerState: LiveData<PlayerState> = mutableLiveDataPlayerState

    private val mutableLiveDataTrackPlaybackProgress = MutableLiveData<Int>().also {
        it.value = getPlayingTrackTimeUseCase.execute()
    }
    val liveDataTrackPlaybackProgress: LiveData<Int> = mutableLiveDataTrackPlaybackProgress

    private val mutableLiveDataIsTrackFavorite = MutableLiveData<Boolean>().also {
        setMutableLiveDataIsTrackFavorite()
    }
    val liveDataIsTrackFavorite: LiveData<Boolean> = mutableLiveDataIsTrackFavorite

    private val playlists = mutableListOf<Playlist>()

    private val mutableLiveDataPlaylists = MutableLiveData<List<Playlist>>(playlists)
    val liveDataPlaylists: LiveData<List<Playlist>> = mutableLiveDataPlaylists

    private val mutableLiveDataTrackPlaylistRelationship = MutableLiveData<TrackPlaylistRelationship>(TrackPlaylistRelationship.TRACK_PRESENCE_IS_UNKNOWN)
    val liveDataTrackPlaylistRelationship: LiveData<TrackPlaylistRelationship> = mutableLiveDataTrackPlaylistRelationship

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

    fun changeTrackFavoriteStatus() {

        viewModelScope.launch(Dispatchers.IO) {

            if (mutableLiveDataIsTrackFavorite.value == true) {
                Log.d("PlayerViewModel", "Removing the track from the DB in thread ${Thread.currentThread().name}")
                mutableLiveDataIsTrackFavorite.postValue(false)
                removeTrackFromFavoritesUseCase.execute(
                    track = Mapper.mapTrackRepresentationToTrack(track)
                )
            } else {
                mutableLiveDataIsTrackFavorite.postValue(true)
                Log.d("PlayerViewModel", "Adding the track to the DB in thread ${Thread.currentThread().name}")
                addTrackToFavoritesUseCase.execute(
                    track = Mapper.mapTrackRepresentationToTrack(track)
                )
            }
        }
    }

    private fun setMutableLiveDataIsTrackFavorite() {

        val tracksIDsFromDB = mutableListOf<Long>()

        viewModelScope.async(Dispatchers.IO) {
            getTracksIDsFromDBUseCase.execute().collect {
                tracksIDsFromDB.addAll(it)
            }
            val isTheTrackInDB = tracksIDsFromDB.contains(track.trackId)
            mutableLiveDataIsTrackFavorite.postValue(isTheTrackInDB)
        }
    }

    fun getPlaylists() {
        playlists.clear()
        viewModelScope.launch(Dispatchers.IO) {
            showPlaylistsUseCase.execute().collect {
                playlists.addAll(it)
                if (playlists.isNotEmpty()) {
                    mutableLiveDataPlaylists.postValue(playlists)
                }
            }
        }
    }

    fun addTrackToPlaylist(playlist: Playlist) {
        if (playlist.tracksId.contains(track.trackId)) {
            mutableLiveDataTrackPlaylistRelationship.value = TrackPlaylistRelationship.TRACK_IS_ALREADY_IN_PLAYLIST
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                addTrackToPlaylistsTracksStorage.execute(track = Mapper.mapTrackRepresentationToTrack(track))
                updatePlaylistUseCase.execute(
                    playlist = playlist,
                    track = Mapper.mapTrackRepresentationToTrack(track)
                )
            }
            mutableLiveDataTrackPlaylistRelationship.postValue(TrackPlaylistRelationship.TRACK_IS_ADDED_TO_PLAYLIST)
        }
    }

    companion object {
        private const val TRACK_TIME_UPDATE_FREQUENCY_MILLIS = 300L
    }

}