package com.example.playlistmaker.ui.search.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.api.ApiResponse
import com.example.playlistmaker.domain.search.use_cases.interfaces.GetHistoryTrackListFromStorageUseCase
import com.example.playlistmaker.domain.search.use_cases.interfaces.GetTracksByApiRequestUseCase
import com.example.playlistmaker.domain.search.use_cases.interfaces.WriteHistoryTrackListToStorageUseCase
import com.example.playlistmaker.ui.mapper.Mapper
import com.example.playlistmaker.ui.models.ITunesServerResponseStatus
import com.example.playlistmaker.ui.models.TrackRepresentation
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Collections

class SearchViewModel(
    private val writeHistoryTrackListToStorageUseCase: WriteHistoryTrackListToStorageUseCase,
    private val getHistoryTrackListFromStorageUseCase: GetHistoryTrackListFromStorageUseCase,
    private val getTracksByApiRequestUseCase: GetTracksByApiRequestUseCase
) : ViewModel() {

    private val mutableLiveDataStatus = MutableLiveData<ITunesServerResponseStatus>()
    val liveDataStatus: LiveData<ITunesServerResponseStatus> = mutableLiveDataStatus

    private val tracks = mutableListOf<TrackRepresentation>()
    private val mutableLiveDataTracks = MutableLiveData<List<TrackRepresentation>>(tracks)
    val liveDataTracks: LiveData<List<TrackRepresentation>> = mutableLiveDataTracks

    private val historyTrackList = getHistoryTrackListFromStorageUseCase.execute().map { Mapper.mapTrackToTrackRepresentation(it) }.toMutableList()
    private val mutableLiveDataHistoryTrackList = MutableLiveData<MutableList<TrackRepresentation>>(historyTrackList)
    val liveDataHistoryTrackList: LiveData<MutableList<TrackRepresentation>> = mutableLiveDataHistoryTrackList

    private val mutableLiveDataSearchRequest = MutableLiveData<String>()
    val liveDataSearchRequest: LiveData<String> = mutableLiveDataSearchRequest

    private val mutableLiveDataIsClickOnTrackAllowed = MutableLiveData<Boolean>(true)
    val liveDataIsClickOnTrackAllowed: LiveData<Boolean> = mutableLiveDataIsClickOnTrackAllowed

    private var searchJob: Job? = null

    fun clearTracks() {
        tracks.clear()
    }

    private fun processSearchedData(data: ApiResponse<List<Track>>) {
        when (data) {
            is ApiResponse.Success -> {
                tracks.clear()
                tracks.addAll(data.data.map { Mapper.mapTrackToTrackRepresentation(it) })
                mutableLiveDataStatus.postValue(ITunesServerResponseStatus.SUCCESS)
            }
            is ApiResponse.EmptyResponse -> {
                tracks.clear()
                mutableLiveDataStatus.postValue(ITunesServerResponseStatus.NOTHING_FOUND)
            }
            is ApiResponse.Error -> {
                tracks.clear()
                mutableLiveDataStatus.postValue(ITunesServerResponseStatus.CONNECTION_ERROR)
            }
        }
    }

    private fun runSearch(searchRequest: String) {
        viewModelScope.launch {
            Log.d("MAKE_NEW_REQUEST", "MAKE_NEW_REQUEST_TO_THE_APPLE_MUSIC in ${Thread.currentThread().name}")
            getTracksByApiRequestUseCase.execute(searchRequest).collect {
                processSearchedData(it)
            }

        }
    }

    fun runSearchWithDelay(searchRequest: String) {
        if (searchRequest.isBlank()) {
            tracks.clear()
            mutableLiveDataStatus.value = ITunesServerResponseStatus.EMPTY_REQUEST
        } else {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(MAKE_REQUEST_DELAY_MILLIS)
                runSearch(searchRequest)
            }
            mutableLiveDataStatus.value = ITunesServerResponseStatus.LOADING
        }
        mutableLiveDataSearchRequest.value = searchRequest
    }

    fun runInstantSearch(searchRequest: String) {
        if (searchRequest.isBlank()) {
            tracks.clear()
            mutableLiveDataStatus.value = ITunesServerResponseStatus.EMPTY_REQUEST
        } else {
            searchJob?.cancel()
            runSearch(searchRequest)
        }
        mutableLiveDataSearchRequest.value = searchRequest
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

    fun writeHistoryTrackListToStorage() {
        writeHistoryTrackListToStorageUseCase.execute(historyTrackList.map {
            Mapper.mapTrackRepresentationToTrack(
                it
            )
        })
    }

    fun fillHistoryTrackListUp(track: TrackRepresentation) {
        val trackIndex = historyTrackList.indexOfFirst { it.trackId == track.trackId }
        if (trackIndex != -1) {
            Collections.swap(historyTrackList, trackIndex, historyTrackList.size-1)
            Log.d("trackAdd", "This track is already on the list. Its position had been updated")
        } else {
            if (historyTrackList.size == HISTORY_TRACK_LIST_SIZE) {
                historyTrackList.removeAt(0)
                Log.d("trackAdd", "Track from list's end deleted")
            }
            historyTrackList.add(track)
            Log.d("trackAdd", "Track added")
        }
    }

    fun clearHistoryTrackList() {
        liveDataHistoryTrackList.value!!.clear()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val MAKE_REQUEST_DELAY_MILLIS = 2000L
        private const val HISTORY_TRACK_LIST_SIZE = 10
    }

}