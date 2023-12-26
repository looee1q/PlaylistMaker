package com.example.playlistmaker.ui.mediateca.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favorites.use_cases.interfaces.ShowAllFavoritesUseCase
import com.example.playlistmaker.ui.mapper.Mapper
import com.example.playlistmaker.ui.mediateca.model.FavoriteStatus
import com.example.playlistmaker.ui.models.TrackRepresentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavouritesViewModel(
    private val showAllTracksFromDBUseCase: ShowAllFavoritesUseCase
) : ViewModel() {

    private val tracks = mutableListOf<TrackRepresentation>()

    private val mutableLiveDataFavoriteTracks = MutableLiveData<List<TrackRepresentation>>(tracks)
    val liveDataFavoriteTracks: LiveData<List<TrackRepresentation>> = mutableLiveDataFavoriteTracks

    private val mutableLiveDataFavoritesStatus = MutableLiveData<FavoriteStatus>()
    val liveDataFavoritesStatus: LiveData<FavoriteStatus> = mutableLiveDataFavoritesStatus

    private val mutableLiveDataIsClickOnTrackAllowed = MutableLiveData<Boolean>(true)
    val liveDataIsClickOnTrackAllowed: LiveData<Boolean> = mutableLiveDataIsClickOnTrackAllowed

    fun showFavorites() {
        tracks.clear()
        viewModelScope.launch(Dispatchers.IO) {
            showAllTracksFromDBUseCase.execute().collect {
                tracks.addAll(
                    it.map { Mapper.mapTrackToTrackRepresentation(it) }
                )
                if (tracks.isEmpty()) {
                    mutableLiveDataFavoritesStatus.postValue(FavoriteStatus.Empty)
                } else {
                    mutableLiveDataFavoritesStatus.postValue(FavoriteStatus.Content(tracks))
                    mutableLiveDataFavoriteTracks.postValue(tracks)
                }
                Log.d("FavoritesViewModel", "Favorites tracks are ${it.map { it.trackName }}")
            }
        }
    }

    fun clickOnTrackDebounce() {
        if (liveDataIsClickOnTrackAllowed.value!!) {
            mutableLiveDataIsClickOnTrackAllowed.value = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                mutableLiveDataIsClickOnTrackAllowed.value = true
            }
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }

}