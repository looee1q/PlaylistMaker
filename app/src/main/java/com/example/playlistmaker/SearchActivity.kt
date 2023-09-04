package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.*
import java.util.Collections

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private lateinit var searchRequest: String

    private lateinit var sharedPreferences: SharedPreferences

    private val tracks = mutableListOf<Track>()
    private val adapter = TrackAdapter(tracks)

    private val historyTrackList = mutableListOf<Track>()
    private val historyTrackListAdapter = TrackAdapter(historyTrackList)

    private var isClickOnTrackAllowed = true

    private val contentType = "application/json".toMediaType()
    private val converterFactory = Json { ignoreUnknownKeys = true }.asConverterFactory(contentType)
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_ITUNES_URL)
        .addConverterFactory(converterFactory)
        .build()

    private val iTunesService = retrofit.create<ITunesApi>()

    private val handlerInMainThread = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(HISTORY_OF_TRACKS, MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPreferences)

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
            searchHistory.getTrackListFromSharedPreferences()
        )

        if (historyTrackList.isNullOrEmpty()) {
            binding.historyTrackListLayout.visibility = View.GONE
        }

        val listener: (Track) -> Unit = {track: Track ->
            if (isClickOnTrackAllowed) {
                clickOnTrackDebounce()
                val intent = Intent(this, TrackInfoActivity::class.java)
                intent.putExtra(TRACK, Json.encodeToString(track))
                startActivity(intent)

                fillHistoryTrackListUp(track)
                searchHistory.writeTrackListToSharedPreferences(historyTrackList)
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
            searchHistory.writeTrackListToSharedPreferences(historyTrackList)
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
                binding.progressBar.visibility = View.VISIBLE
                hideErrorsButtons()
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
        iTunesService.search(searchRequest)
            .enqueue(object : Callback<ITunesServerResponse> {
                override fun onResponse(
                    call: Call<ITunesServerResponse>,
                    response: Response<ITunesServerResponse>
                ) {
                    binding.progressBar.visibility = View.GONE
                    if (response.code() == CODE_200) {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            Log.d("ServerResponseIsSucceed", "${response.body()?.results!![0]}")
                            tracks.clear()
                            tracks.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                            showError(ITunesServerResponseStatus.SUCCESS)
                        } else {
                            if (searchRequest.isNotBlank()) {
                                showError(ITunesServerResponseStatus.NOTHING_FOUND)
                            }
                        }
                    } else {
                        showError(ITunesServerResponseStatus.CONNECTION_ERROR)
                        binding.reloadSearchButton.setOnClickListener {
                            runSearch()
                        }
                    }
                    Log.d("ServerResponse", "Ya Iskal")
                }

                override fun onFailure(call: Call<ITunesServerResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    Log.d("ServerResponseIsFailed", "${t.message}")
                    showError(ITunesServerResponseStatus.CONNECTION_ERROR)
                    binding.reloadSearchButton.setOnClickListener {
                        runSearch()
                    }
                }
            })
    }

    private fun clickOnTrackDebounce() {
        if (isClickOnTrackAllowed) {
            isClickOnTrackAllowed = false
            handlerInMainThread.postDelayed({ isClickOnTrackAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
        }
    }
    private fun fillHistoryTrackListUp(track: Track) {
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
        fun clearTracksAndSetErrorViews(errorCover: Int, errorText: Int) {
            tracks.clear()
            adapter.notifyDataSetChanged()
            binding.errorImage.visibility = View.VISIBLE
            binding.errorMessage.visibility = View.VISIBLE
            binding.errorImage.setImageDrawable(getDrawable(errorCover))
            binding.errorMessage.text = getText(errorText)
        }

        when (iTunesServerResponseStatus) {
            ITunesServerResponseStatus.CONNECTION_ERROR -> {
                clearTracksAndSetErrorViews(R.drawable.connection_error, R.string.connection_error)
                binding.reloadSearchButton.visibility = View.VISIBLE
            }
            ITunesServerResponseStatus.NOTHING_FOUND -> {
                clearTracksAndSetErrorViews(R.drawable.nothing_found_error, R.string.nothing_found)
            }
            ITunesServerResponseStatus.SUCCESS -> {
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
        private const val BASE_ITUNES_URL: String = "https://itunes.apple.com"
        private const val INPUT_IN_SEARCH_ACTIVITY = "INPUT_IN_SEARCH_ACTIVITY"
        private const val CODE_200 = 200
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val MAKE_REQUEST_DELAY_MILLIS = 2000L
        private const val HISTORY_TRACK_LIST_SIZE = 10
        private const val HISTORY_OF_TRACKS = "HISTORY_OF_TRACKS"
        const val TRACK = "TRACK"
    }

}