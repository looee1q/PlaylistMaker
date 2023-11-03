package com.example.playlistmaker.ui.search.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.ui.models.ITunesServerResponseStatus
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.ui.models.TrackRepresentation
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private val viewModel: SearchViewModel by viewModel()

    private lateinit var adapter: TrackAdapter

    private lateinit var historyTrackListAdapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        Log.d("SearchFragment", "onCreateView in SearchFragment")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("SearchFragment", "onViewCreated in SearchFragment")

        viewModel.liveDataStatus.observe(viewLifecycleOwner) {
            Log.d("CURRENT_STATUS", "$it in SearchFragment")
            render(it)
        }

        adapter = TrackAdapter(viewModel.liveDataTracks.value!!)

        val historyTrackList = viewModel.liveDataHistoryTrackList.value!!
        historyTrackListAdapter = TrackAdapter(historyTrackList)

        binding.backToMainScreen.setOnClickListener {
            findNavController().navigateUp()
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

        binding.trackListRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.listener = createAdapterListener()
        binding.trackListRecyclerView.adapter = adapter

        binding.historyTrackListRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
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

    private fun createAdapterListener(): (TrackRepresentation) -> Unit {
        return { track: TrackRepresentation ->
            if (viewModel.liveDataIsClickOnTrackAllowed.value!!) {
                viewModel.clickOnTrackDebounce()
                findNavController().navigate(
                    R.id.action_searchFragment_to_playerActivity,
                    PlayerActivity.createArgs(Json.encodeToString(track))
                )

                viewModel.fillHistoryTrackListUp(track)
                viewModel.writeHistoryTrackListToStorage()
                historyTrackListAdapter.notifyDataSetChanged()
                Log.d("historyTrackList","${track.trackName} with Id ${track.trackId} has been added to historyTrackList")
            }
        }
    }

    private fun closeKeyboard() {
        val currentView = requireActivity().currentFocus
        if (currentView != null) {
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(`currentView`.windowToken, 0)
        }
    }

    private fun render(iTunesServerResponseStatus: ITunesServerResponseStatus) {

        when (iTunesServerResponseStatus) {
            ITunesServerResponseStatus.CONNECTION_ERROR -> {
                adapter.notifyDataSetChanged()

                binding.progressBar.visibility = View.GONE
                binding.errorImage.visibility = View.VISIBLE
                binding.errorMessage.visibility = View.VISIBLE
                binding.errorImage.setImageDrawable(requireContext().getDrawable(R.drawable.connection_error))
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
                binding.errorImage.setImageDrawable(requireContext().getDrawable(R.drawable.nothing_found_error))
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



}