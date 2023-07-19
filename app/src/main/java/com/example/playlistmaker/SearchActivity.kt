package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isNotEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Collections

private const val BASE_ITUNES_URL: String = "https://itunes.apple.com"
private const val INPUT_IN_SEARCH_ACTIVITY = "INPUT_IN_SEARCH_ACTIVITY"
private const val CODE_200 = 200
private const val HISTORY_TRACK_LIST_SIZE = 10
const val HISTORY_OF_TRACKS = "HISTORY_OF_TRACKS"

class SearchActivity : AppCompatActivity() {
    private lateinit var tracksSearchField: EditText
    private lateinit var trackList: RecyclerView
    private lateinit var searchRequest: String
    private lateinit var errorImage: ImageView
    private lateinit var errorMessage: TextView
    private lateinit var reloadSearchButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var historyTrackListLayout: ConstraintLayout
    private lateinit var historyTrackListRecyclerView: RecyclerView
    private lateinit var clearHistoryButton: Button

    private val tracks = mutableListOf<Track>()
    private val adapter = TrackAdapter(tracks)

    private val historyTrackList = mutableListOf<Track>()
    private val historyTrackListAdapter = TrackAdapter(historyTrackList)

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_ITUNES_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create<ITunesApi>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        sharedPreferences = getSharedPreferences(HISTORY_OF_TRACKS, MODE_PRIVATE)

        val backToMainScreen = findViewById<ImageView>(R.id.to_main_screen_from_search)
        val clearingCross = findViewById<ImageView>(R.id.clearingCrossInSettingActivity)
        tracksSearchField = findViewById<EditText>(R.id.searchEditTextInSettingsActivity)
        trackList = findViewById<RecyclerView>(R.id.track_list_recycler_view)
        errorImage = findViewById<ImageView>(R.id.error_image)
        errorMessage = findViewById<TextView>(R.id.error_message)
        reloadSearchButton = findViewById<Button>(R.id.reload_search_button)
        historyTrackListLayout = findViewById<ConstraintLayout>(R.id.history_of_tracks_constraint_layout)
        historyTrackListRecyclerView = findViewById<RecyclerView>(R.id.history_of_tracks_list_recycler_view)
        clearHistoryButton = findViewById<Button>(R.id.clear_history_button)

        val searchHistory = SearchHistory(sharedPreferences)

        searchRequest = tracksSearchField.text.toString()

        backToMainScreen.setOnClickListener {
            //val displayIntent = Intent(this, MainActivity::class.java)
            //startActivity(displayIntent)
            finish()
        }

        clearingCross.setOnClickListener {
            tracksSearchField.setText("")
            tracks.clear()
            adapter.notifyDataSetChanged()
            closeKeyboard()
            if (historyTrackList.isNotEmpty()) historyTrackListLayout.visibility = View.VISIBLE
        }

        historyTrackList.addAll(
            searchHistory.getTrackListFromSharedPreferences()
        )

        if (historyTrackList.isNullOrEmpty()) {
            historyTrackListLayout.visibility = View.GONE
        }

        trackList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter.listener = {track: Track ->
            fillHistoryTrackListUp(track)
            searchHistory.writeTrackListToSharedPreferences(historyTrackList)
            historyTrackListAdapter.notifyDataSetChanged()
            Log.d("historyTrackList", "${track} with Id ${track.trackId} has been added to historyTrackList")
        }

        trackList.adapter = adapter

        historyTrackListRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        historyTrackListRecyclerView.adapter = historyTrackListAdapter

        clearHistoryButton.setOnClickListener {
            historyTrackList.clear()
            searchHistory.writeTrackListToSharedPreferences(historyTrackList)
            historyTrackListAdapter.notifyDataSetChanged()
            historyTrackListLayout.visibility = View.GONE
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(input: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (tracksSearchField.hasFocus() && input.isNullOrEmpty() && historyTrackList.isNotEmpty()) {
                    historyTrackListLayout.visibility = View.VISIBLE
                } else {
                    historyTrackListLayout.visibility = View.GONE
                }

                if (input.isNullOrEmpty()) clearingCross.visibility = View.GONE
                else clearingCross.visibility = View.VISIBLE
                searchRequest = input.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }

        tracksSearchField.addTextChangedListener(textWatcher)

        tracksSearchField.setOnFocusChangeListener { view, hasFocus ->
            historyTrackListLayout.visibility = if (hasFocus
                && tracksSearchField.text.isEmpty()
                && historyTrackList.isNotEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        fun runSearch() {
            iTunesService.search(searchRequest)
                .enqueue(object : Callback<ITunesServerResponse> {
                    override fun onResponse(
                        call: Call<ITunesServerResponse>,
                        response: Response<ITunesServerResponse>
                    ) {
                        if (response.code() == CODE_200) {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.clear()
                                tracks.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                                showError(ITunesServerResponseStatus.SUCCESS)
                            } else {
                                showError(ITunesServerResponseStatus.NOTHING_FOUND)
                            }
                        } else {
                            showError(ITunesServerResponseStatus.CONNECTION_ERROR)
                            reloadSearchButton.setOnClickListener {
                                runSearch()
                            }
                        }
                        Log.d("Search", "Ya Iskal")
                    }

                    override fun onFailure(call: Call<ITunesServerResponse>, t: Throwable) {
                        showError(ITunesServerResponseStatus.CONNECTION_ERROR)
                        reloadSearchButton.setOnClickListener {
                            runSearch()
                        }
                    }
                })
        }

        tracksSearchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (searchRequest.isNotEmpty()) {
                    historyTrackListLayout.visibility = View.GONE
                    runSearch()
                }
                true
            }
            false
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
        tracksSearchField.setText(savedInstanceState.getString(INPUT_IN_SEARCH_ACTIVITY))
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
            errorImage.visibility = View.VISIBLE
            errorMessage.visibility = View.VISIBLE
            errorImage.setImageDrawable(getDrawable(errorCover))
            errorMessage.text = getText(errorText)
        }

        when (iTunesServerResponseStatus) {
            ITunesServerResponseStatus.CONNECTION_ERROR -> {
                clearTracksAndSetErrorViews(R.drawable.connection_error, R.string.connection_error)
                reloadSearchButton.visibility = View.VISIBLE
            }
            ITunesServerResponseStatus.NOTHING_FOUND -> {
                clearTracksAndSetErrorViews(R.drawable.nothing_found_error, R.string.nothing_found)
            }
            ITunesServerResponseStatus.SUCCESS -> {
                errorImage.visibility = View.GONE
                errorMessage.visibility = View.GONE
                reloadSearchButton.visibility = View.GONE
            }
        }
    }

}