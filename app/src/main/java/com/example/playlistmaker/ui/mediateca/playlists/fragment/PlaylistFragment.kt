package com.example.playlistmaker.ui.mediateca.playlists.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.ui.mediateca.playlists.model.PlaylistInfo
import com.example.playlistmaker.ui.mediateca.playlists.view_model.PlaylistViewModel
import com.example.playlistmaker.ui.models.TrackRepresentation
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import com.example.playlistmaker.ui.search.fragment.TrackAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private var playlistId: Int = -1

    private val viewModel: PlaylistViewModel by viewModel { parametersOf(playlistId) }

    private var _adapter: TrackAdapter? = null
    private val adapter get() = _adapter!!

    private val playlistTracks: MutableList<TrackRepresentation> = mutableListOf()

    private var deleteTrackDialog: MaterialAlertDialogBuilder? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
/*        val screenHeight = binding.coordinator.height
        Log.d("PlaylistFragment","screenHeight is $screenHeight")
        val b = BottomSheetBehavior.from(binding.bottomSheet)
        b.peekHeight = resources.getDimension(R.dimen.peek_height_for_bottom_sheet_in_playlist).toInt()*/
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistId = arguments?.getInt(PLAYLIST_ID)!!

        _adapter = TrackAdapter(playlistTracks)
        adapter.listener = createAdapterListener()
        adapter.longClickListener = createLongClickAdapterListener()
        binding.recyclerViewTracks.adapter = adapter
        binding.recyclerViewTracks.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })

        binding.backArrowButton.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.liveDataPlaylist.observe(viewLifecycleOwner) {
            render(it)
            Log.d("PlaylistFragment","Tracks from PlaylistInfo ${it.tracks.map{it.trackName}}")
            //Log.d("PlaylistFragment","Adapter Track List 2: ${_adapter?.trackList?.map{it.trackName}}")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }

    private fun createAdapterListener(): (TrackRepresentation) -> Unit {
        return { track: TrackRepresentation ->
            if (viewModel.liveDataIsClickOnTrackAllowed.value!!) {
                viewModel.clickOnTrackDebounce()

                findNavController().navigate(
                    R.id.action_playlistFragment_to_playerActivity,
                    PlayerActivity.createArgs(Json.encodeToString(track))
                )
            }
        }
    }

    private fun createLongClickAdapterListener(): (TrackRepresentation) -> Unit {
        return { trackRepresentation: TrackRepresentation ->
            deleteTrackDialog = createConfirmTrackRemovalDialog(trackRepresentation)
            deleteTrackDialog?.show()
        }
    }

    private fun createConfirmTrackRemovalDialog(trackRepresentation: TrackRepresentation): MaterialAlertDialogBuilder {
        val dialogListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    viewModel.deleteTrack(trackRepresentation)
                }
            }
        }
        return MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.do_you_want_to_delete_track))
            .setPositiveButton(resources.getString(R.string.yes), dialogListener)
            .setNegativeButton(resources.getString(R.string.no), dialogListener)
    }


    private fun render(playlistInfo: PlaylistInfo) {

        val playlist = playlistInfo.playlist

        Glide.with(this)
            .load(playlist.coverUri)
            .apply(
                RequestOptions().placeholder(R.drawable.track_icon_mock)
            )
            .transform(
                CenterCrop(),
            )
            .into(binding.playlistCover)

        binding.apply {
            playlistTitle.text = playlist.title
            if (!playlist.description.isNullOrBlank()) {
                Log.d("PlaylistFragment", "playlist description is ${playlist.description}")
                playlistDescription.text = playlist.description
                playlistDescription.isVisible = true
            }
            playlistDuration.text = resources.getQuantityString(R.plurals.minute_plurals, playlistInfo.durationInMinutes, playlistInfo.durationInMinutes)
            playlistSize.text = resources.getQuantityString(R.plurals.track_plurals, playlist.size, playlist.size)
        }

        playlistTracks.clear()
        playlistTracks.addAll(playlistInfo.tracks)
        adapter?.notifyDataSetChanged()
    }

    companion object {

        const val PLAYLIST_ID = "PLAYLIST_ID"

        fun createArgs(playlistId: Int) = bundleOf(PLAYLIST_ID to playlistId)
    }

}