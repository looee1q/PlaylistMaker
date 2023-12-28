package com.example.playlistmaker.ui.mediateca.favorites.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.mediateca.favorites.use_cases.interfaces.ShowAllFavoritesUseCase
import com.example.playlistmaker.ui.mapper.Mapper
import com.example.playlistmaker.ui.mediateca.model.Status
import com.example.playlistmaker.ui.models.TrackRepresentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavouritesViewModel(
    private val showAllFavoritesUseCase: ShowAllFavoritesUseCase
) : ViewModel() {

    private val tracks = mutableListOf<TrackRepresentation>()

    private val mutableLiveDataFavoriteTracks = MutableLiveData<List<TrackRepresentation>>(tracks)
    val liveDataFavoriteTracks: LiveData<List<TrackRepresentation>> = mutableLiveDataFavoriteTracks

    private val mutableLiveDataFavoritesStatus = MutableLiveData<Status<List<TrackRepresentation>>>()
    val liveDataFavoritesStatus: LiveData<Status<List<TrackRepresentation>>> = mutableLiveDataFavoritesStatus

    private val mutableLiveDataIsClickOnTrackAllowed = MutableLiveData<Boolean>(true)
    val liveDataIsClickOnTrackAllowed: LiveData<Boolean> = mutableLiveDataIsClickOnTrackAllowed

    fun showFavorites() {
        tracks.clear()
        viewModelScope.launch(Dispatchers.IO) {
            showAllFavoritesUseCase.execute().collect {
                tracks.addAll(
                    it.map { Mapper.mapTrackToTrackRepresentation(it) }
                )
                if (tracks.isEmpty()) {
                    mutableLiveDataFavoritesStatus.postValue(Status.Empty())
                } else {
                    mutableLiveDataFavoritesStatus.postValue(Status.Content(tracks))
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