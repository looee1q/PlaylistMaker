package com.example.playlistmaker.ui.mediateca.playlists.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.ui.mediateca.playlists.model.PlaylistInfo
import com.example.playlistmaker.ui.mediateca.playlists.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private var trackId: Int = -1

    private val viewModel: PlaylistViewModel by viewModel { parametersOf(trackId) }

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

        trackId = arguments?.getInt(PLAYLIST_ID)!!

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
        }
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

        binding.playlistTitle.text = playlist.title
        if (!playlist.description.isNullOrBlank()) {
            Log.d("PlaylistFragment", "playlist description is ${playlist.description}")
            binding.playlistDescription.text = playlist.description
            binding.playlistDescription.isVisible = true
        }
        binding.playlistDuration.text = playlistInfo.duration
        binding.playlistSize.text = resources.getQuantityString(R.plurals.track_plurals, playlist.size, playlist.size)
    }

    companion object {

        const val PLAYLIST_ID = "PLAYLIST_ID"

        fun createArgs(playlistId: Int) = bundleOf(PLAYLIST_ID to playlistId)
    }

}