package com.example.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.ui.models.ITunesServerResponseStatus
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.search.consumer.Consumer
import com.example.playlistmaker.domain.search.consumer.ConsumerData
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.mapper.Mapper
import com.example.playlistmaker.ui.models.TrackActivity
import com.example.playlistmaker.ui.player.TrackInfoActivity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Collections

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private lateinit var searchRequest: String

    private val writeHistoryTrackListToStorageUseCase by lazy {
        Creator.provideWriteHistoryTrackListToStorageUseCase()
    }
    private val getHistoryTrackListFromStorageUseCase by lazy {
        Creator.provideGetHistoryTrackListFromStorageUseCase()
    }

    private val tracks = mutableListOf<TrackActivity>()
    private val adapter = TrackAdapter(tracks)

    private val historyTrackList = mutableListOf<TrackActivity>()
    private val historyTrackListAdapter = TrackAdapter(historyTrackList)

    private val getTracksByApiRequestUseCase = Creator.provideGetTracksByApiRequestUseCase()

    private val handlerInMainThread = Handler(Looper.getMainLooper())

    private var isClickOnTrackAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchRequest = binding.tracksSearchField.text.toString()

        binding.backToMainScreen.setOnClickListener {
            finish()
        }

        binding.clearingHistoryCross.setOnClickListener {
            binding.tracksSearchField.setText("")
            tracks.clear()
            adapter.notifyDataSetChanged()
            closeKeyboard()
            if (historyTrackList.isNotEmpty()) binding.historyTrackListLayout.visibility = View.VISIBLE
        }

        historyTrackList.addAll(
            getHistoryTrackListFromStorageUseCase.execute().map { Mapper.mapTrackToTrackActivity(it) }
        )

        if (historyTrackList.isNullOrEmpty()) {
            binding.historyTrackListLayout.visibility = View.GONE
        }

        val listener: (TrackActivity) -> Unit = { track: TrackActivity ->
            if (isClickOnTrackAllowed) {
                clickOnTrackDebounce()
                val intent = Intent(this, TrackInfoActivity::class.java)
                intent.putExtra(TRACK, Json.encodeToString(track))
                startActivity(intent)

                fillHistoryTrackListUp(track)
                writeHistoryTrackListToStorageUseCase.execute(historyTrackList.map { Mapper.mapTrackActivityToTrack(it) })
                historyTrackListAdapter.notifyDataSetChanged()
                Log.d(
                    "historyTrackList",
                    "${track} with Id ${track.trackId} has been added to historyTrackList"
                )
            }
        }

        binding.trackListRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter.listener = listener
        binding.trackListRecyclerView.adapter = adapter

        binding.historyTrackListRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        historyTrackListAdapter.listener = listener
        binding.historyTrackListRecyclerView.adapter = historyTrackListAdapter

        binding.clearHistoryButton.setOnClickListener {
            historyTrackList.clear()
            writeHistoryTrackListToStorageUseCase.execute(historyTrackList.map { Mapper.mapTrackActivityToTrack(it) })
            historyTrackListAdapter.notifyDataSetChanged()
            binding.historyTrackListLayout.visibility = View.GONE
        }

        val searchRunnable = Runnable { runSearch() }

        binding.tracksSearchField.doOnTextChanged { input, start, before, count ->
            if (binding.tracksSearchField.hasFocus() && input.isNullOrEmpty() && historyTrackList.isNotEmpty()) {
                binding.historyTrackListLayout.visibility = View.VISIBLE
            } else {
                binding.historyTrackListLayout.visibility = View.GONE
            }

            if (input.isNullOrEmpty()) binding.clearingHistoryCross.visibility = View.GONE
            else binding.clearingHistoryCross.visibility = View.VISIBLE

            searchRequest = input.toString()

            if (searchRequest.isBlank()) {
                tracks.clear()
                hideErrorsButtons()
            } else {
                handlerInMainThread.removeCallbacks(searchRunnable)
                handlerInMainThread.postDelayed(searchRunnable, MAKE_REQUEST_DELAY_MILLIS)
                showError(ITunesServerResponseStatus.LOADING)
            }
        }

        binding.tracksSearchField.setOnFocusChangeListener { view, hasFocus ->
            binding.historyTrackListLayout.visibility = if (hasFocus
                && binding.tracksSearchField.text.isEmpty()
                && historyTrackList.isNotEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        binding.tracksSearchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (searchRequest.isNotBlank()) {
                    handlerInMainThread.removeCallbacks(searchRunnable)
                    runSearch()
                }
                true
            }
            false
        }

    }

    private fun runSearch() {
        getTracksByApiRequestUseCase.execute(
            request = searchRequest,
            consumer = object : Consumer<List<Track>> {
                override fun consume(data: ConsumerData<List<Track>>) {
                    val consumeRunnable = Runnable {
                        Log.d("CONSUME_RUNNABLE", "inside consume runnable in ${Thread.currentThread().name}")
                        when (data) {
                            is ConsumerData.Data -> {
                                tracks.clear()
                                tracks.addAll(data.data.map { Mapper.mapTrackToTrackActivity(it) })
                                adapter.notifyDataSetChanged()
                                showError(ITunesServerResponseStatus.SUCCESS)
                            }
                            is ConsumerData.EmptyData -> {
                                tracks.clear()
                                adapter.notifyDataSetChanged()
                                showError(ITunesServerResponseStatus.NOTHING_FOUND)
                            }
                            is ConsumerData.Error -> {
                                tracks.clear()
                                adapter.notifyDataSetChanged()
                                showError(ITunesServerResponseStatus.CONNECTION_ERROR)
                                binding.reloadSearchButton.setOnClickListener {
                                    runSearch()
                                }
                            }
                        }
                    }

                    handlerInMainThread.post(consumeRunnable)
                }

            }
        )
    }
    private fun clickOnTrackDebounce() {
        if (isClickOnTrackAllowed) {
            isClickOnTrackAllowed = false
            handlerInMainThread.postDelayed({ isClickOnTrackAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
        }
    }
    private fun fillHistoryTrackListUp(track: TrackActivity) {
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_IN_SEARCH_ACTIVITY, searchRequest)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.tracksSearchField.setText(savedInstanceState.getString(INPUT_IN_SEARCH_ACTIVITY))
    }

    private fun closeKeyboard() {
        val currentView = this.currentFocus
        if (currentView != null) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(`currentView`.windowToken, 0)
        }
    }

    private fun showError(iTunesServerResponseStatus: ITunesServerResponseStatus) {

        when (iTunesServerResponseStatus) {
            ITunesServerResponseStatus.CONNECTION_ERROR -> {
                binding.progressBar.visibility = View.GONE
                binding.errorImage.visibility = View.VISIBLE
                binding.errorMessage.visibility = View.VISIBLE
                binding.errorImage.setImageDrawable(getDrawable(R.drawable.connection_error))
                binding.errorMessage.text = getText(R.string.connection_error)
                binding.reloadSearchButton.visibility = View.VISIBLE
            }
            ITunesServerResponseStatus.NOTHING_FOUND -> {
                binding.progressBar.visibility = View.GONE
                binding.errorImage.visibility = View.VISIBLE
                binding.errorMessage.visibility = View.VISIBLE
                binding.errorImage.setImageDrawable(getDrawable(R.drawable.nothing_found_error))
                binding.errorMessage.text = getText(R.string.nothing_found)
            }
            ITunesServerResponseStatus.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
                hideErrorsButtons()
            }
            ITunesServerResponseStatus.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
                hideErrorsButtons()
            }

        }
    }

    private fun hideErrorsButtons() {
        binding.errorImage.visibility = View.GONE
        binding.errorMessage.visibility = View.GONE
        binding.reloadSearchButton.visibility = View.GONE
    }

    companion object {
        private const val INPUT_IN_SEARCH_ACTIVITY = "INPUT_IN_SEARCH_ACTIVITY"
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val MAKE_REQUEST_DELAY_MILLIS = 2000L
        private const val HISTORY_TRACK_LIST_SIZE = 10
        const val TRACK = "TRACK"
    }

}