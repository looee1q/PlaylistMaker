package com.example.playlistmaker.ui.search.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.ui.models.ITunesServerResponseStatus
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.ui.models.TrackActivity
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModelFactory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private lateinit var viewModel: SearchViewModel

    private lateinit var adapter: TrackAdapter

    private lateinit var historyTrackListAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SearchViewModelFactory()).get(SearchViewModel::class.java)

        viewModel.liveDataStatus.observe(this) {
            Log.d("CURRENT_STATUS", "$it")
            showError(it)
        }

        adapter = TrackAdapter(viewModel.liveDataTracks.value!!)

        val historyTrackList = viewModel.liveDataHistoryTrackList.value!!
        historyTrackListAdapter = TrackAdapter(historyTrackList)

        binding.backToMainScreen.setOnClickListener {
            finish()
        }

        binding.tracksSearchField.setText(viewModel.liveDataSearchRequest.value)

        binding.clearingRequestCross.setOnClickListener {
            binding.tracksSearchField.setText("")
            viewModel.clearTracks()
            adapter.notifyDataSetChanged()
            closeKeyboard()
            if (historyTrackList.isNotEmpty()) binding.historyTrackListLayout.visibility = View.VISIBLE
        }

        if (historyTrackList.isEmpty()) {
            binding.historyTrackListLayout.visibility = View.GONE
        }

        binding.trackListRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter.listener = createAdapterListener()
        binding.trackListRecyclerView.adapter = adapter

        binding.historyTrackListRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        historyTrackListAdapter.listener = createAdapterListener()
        binding.historyTrackListRecyclerView.adapter = historyTrackListAdapter

        binding.clearHistoryTrackListButton.setOnClickListener {
            viewModel.clearHistoryTrackList()
            viewModel.writeHistoryTrackListToStorage()
            historyTrackListAdapter.notifyDataSetChanged()
            binding.historyTrackListLayout.visibility = View.GONE
        }

        binding.tracksSearchField.doOnTextChanged { input, start, before, count ->
            if (binding.tracksSearchField.hasFocus() && input.isNullOrEmpty() && historyTrackList.isNotEmpty()) {
                binding.historyTrackListLayout.visibility = View.VISIBLE
            } else {
                binding.historyTrackListLayout.visibility = View.GONE
            }

            if (input.isNullOrEmpty()) binding.clearingRequestCross.visibility = View.GONE
            else binding.clearingRequestCross.visibility = View.VISIBLE

            viewModel.runSearchWithDelay(input.toString())
        }

        binding.tracksSearchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.runInstantSearch(binding.tracksSearchField.text.toString())
                true
            }
            false
        }

        binding.tracksSearchField.setOnFocusChangeListener { view, hasFocus ->
            binding.historyTrackListLayout.visibility = if (hasFocus
                && binding.tracksSearchField.text.isEmpty()
                && historyTrackList.isNotEmpty())
            {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

    }

    private fun createAdapterListener(): (TrackActivity) -> Unit {
        return { track: TrackActivity ->
            if (viewModel.liveDataIsClickOnTrackAllowed.value!!) {
                viewModel.clickOnTrackDebounce()
                val intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra(TRACK, Json.encodeToString(track))
                startActivity(intent)

                viewModel.fillHistoryTrackListUp(track)
                viewModel.writeHistoryTrackListToStorage()
                historyTrackListAdapter.notifyDataSetChanged()
                Log.d("historyTrackList","${track.trackName} with Id ${track.trackId} has been added to historyTrackList")
            }
        }
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
                adapter.notifyDataSetChanged()

                binding.progressBar.visibility = View.GONE
                binding.errorImage.visibility = View.VISIBLE
                binding.errorMessage.visibility = View.VISIBLE
                binding.errorImage.setImageDrawable(getDrawable(R.drawable.connection_error))
                binding.errorMessage.text = getText(R.string.connection_error)
                binding.reloadSearchButton.visibility = View.VISIBLE

                binding.reloadSearchButton.setOnClickListener {
                    viewModel.runInstantSearch(binding.tracksSearchField.text.toString())
                }
            }
            ITunesServerResponseStatus.NOTHING_FOUND -> {
                adapter.notifyDataSetChanged()

                binding.progressBar.visibility = View.GONE
                binding.errorImage.visibility = View.VISIBLE
                binding.errorMessage.visibility = View.VISIBLE
                binding.errorImage.setImageDrawable(getDrawable(R.drawable.nothing_found_error))
                binding.errorMessage.text = getText(R.string.nothing_found)
            }
            ITunesServerResponseStatus.SUCCESS -> {
                adapter.notifyDataSetChanged()

                binding.progressBar.visibility = View.GONE
                hideErrorsButtons()
            }
            ITunesServerResponseStatus.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
                hideErrorsButtons()
            }
            ITunesServerResponseStatus.EMPTY_REQUEST -> {
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
        const val TRACK = "TRACK"
    }

}