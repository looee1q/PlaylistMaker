package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.consumer.Consumer
import com.example.playlistmaker.domain.search.consumer.ConsumerData
import com.example.playlistmaker.domain.search.use_cases.interfaces.GetHistoryTrackListFromStorageUseCase
import com.example.playlistmaker.domain.search.use_cases.interfaces.GetTracksByApiRequestUseCase
import com.example.playlistmaker.domain.search.use_cases.interfaces.WriteHistoryTrackListToStorageUseCase
import com.example.playlistmaker.ui.mapper.Mapper
import com.example.playlistmaker.ui.models.ITunesServerResponseStatus
import com.example.playlistmaker.ui.models.TrackRepresentation
import java.util.Collections

class SearchViewModel(
    private val writeHistoryTrackListToStorageUseCase: WriteHistoryTrackListToStorageUseCase,
    private val getHistoryTrackListFromStorageUseCase: GetHistoryTrackListFromStorageUseCase,
    private val getTracksByApiRequestUseCase: GetTracksByApiRequestUseCase
) : ViewModel() {


    private val handlerInMainThread = Handler(Looper.getMainLooper())

    private val mutableLiveDataStatus = MutableLiveData<ITunesServerResponseStatus>()
    val liveDataStatus: LiveData<ITunesServerResponseStatus> = mutableLiveDataStatus

    private val tracks = mutableListOf<TrackRepresentation>()
    private val mutableLiveDataTracks = MutableLiveData<MutableList<TrackRepresentation>>(tracks)
    val liveDataTracks: LiveData<MutableList<TrackRepresentation>> = mutableLiveDataTracks

    private val historyTrackList = getHistoryTrackListFromStorageUseCase.execute().map { Mapper.mapTrackToTrackRepresentation(it) }.toMutableList()
    private val mutableLiveDataHistoryTrackList = MutableLiveData<MutableList<TrackRepresentation>>(historyTrackList)
    val liveDataHistoryTrackList: LiveData<MutableList<TrackRepresentation>> = mutableLiveDataHistoryTrackList

    private val mutableLiveDataSearchRequest = MutableLiveData<String>()
    val liveDataSearchRequest: LiveData<String> = mutableLiveDataSearchRequest

    private val mutableLiveDataIsClickOnTrackAllowed = MutableLiveData<Boolean>(true)
    val liveDataIsClickOnTrackAllowed: LiveData<Boolean> = mutableLiveDataIsClickOnTrackAllowed

    private var searchRunnable = Runnable{ }

    fun clearTracks() {
        tracks.clear()
    }

    private fun runSearch(searchRequest: String) {
        Log.d("MAKE_NEW_REQUEST", "MAKE_NEW_REQUEST_TO_THE_APPLE_MUSIC")
        getTracksByApiRequestUseCase.execute(
            request = searchRequest,
            consumer = object : Consumer<List<Track>> {
                override fun consume(data: ConsumerData<List<Track>>) {
                    val consumeRunnable = Runnable {
                        Log.d("CONSUME_RUNNABLE", "inside consume runnable in ${Thread.currentThread().name}")
                        when (data) {
                            is ConsumerData.Data -> {
                                tracks.clear()
                                tracks.addAll(data.data.map { Mapper.mapTrackToTrackRepresentation(it) })
                                mutableLiveDataStatus.value = ITunesServerResponseStatus.SUCCESS
                            }
                            is ConsumerData.EmptyData -> {
                                tracks.clear()
                                mutableLiveDataStatus.value = ITunesServerResponseStatus.NOTHING_FOUND
                            }
                            is ConsumerData.Error -> {
                                tracks.clear()
                                mutableLiveDataStatus.value = ITunesServerResponseStatus.CONNECTION_ERROR
                            }
                        }
                    }

                    handlerInMainThread.post(consumeRunnable)
                }

            }
        )
    }

    fun runSearchWithDelay(searchRequest: String) {
        if (searchRequest.isBlank()) {
            tracks.clear()
            mutableLiveDataStatus.value = ITunesServerResponseStatus.EMPTY_REQUEST
        } else {
            handlerInMainThread.removeCallbacks(searchRunnable)
            searchRunnable = Runnable { runSearch(searchRequest) }
            handlerInMainThread.postDelayed(searchRunnable,
                MAKE_REQUEST_DELAY_MILLIS
            )
            mutableLiveDataStatus.value = ITunesServerResponseStatus.LOADING
        }
        mutableLiveDataSearchRequest.value = searchRequest
    }

    fun runInstantSearch(searchRequest: String) {
        if (searchRequest.isBlank()) {
            tracks.clear()
            mutableLiveDataStatus.value = ITunesServerResponseStatus.EMPTY_REQUEST
        } else {
            handlerInMainThread.removeCallbacks(searchRunnable)
            runSearch(searchRequest)
        }
        mutableLiveDataSearchRequest.value = searchRequest
    }

    fun clickOnTrackDebounce() {
        if (liveDataIsClickOnTrackAllowed.value!!) {
            mutableLiveDataIsClickOnTrackAllowed.value = false
            handlerInMainThread.postDelayed({ mutableLiveDataIsClickOnTrackAllowed.value = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
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