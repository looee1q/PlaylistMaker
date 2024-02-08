package com.example.playlistmaker.ui.mediateca.playlists.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.ui.mediateca.playlists.model.PlaylistInfo
import com.example.playlistmaker.ui.mediateca.playlists.view_model.PlaylistViewModel
import com.example.playlistmaker.ui.models.TrackRepresentation
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import com.example.playlistmaker.ui.search.fragment.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
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

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("LifecycleFragment", "onCreateView || PlaylistFragment")
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("LifecycleFragment", "onViewCreated || PlaylistFragment")
        playlistId = arguments?.getInt(PLAYLIST_ID)!!

        _adapter = TrackAdapter(playlistTracks)
        adapter.listener = createAdapterListener()
        adapter.longClickListener = createLongClickAdapterListener()
        binding.recyclerViewTracks.adapter = adapter
        binding.recyclerViewTracks.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, true
        ).also { it.stackFromEnd = true }

        binding.backArrowButton.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.getPlaylistInfo()

        viewModel.liveDataPlaylist.observe(viewLifecycleOwner) {
            render(it)
            renderPlaylistInfoForBottomSheet(it)
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetMenu).also {
                it.state = BottomSheetBehavior.STATE_HIDDEN
                Log.d("PlaylistFragment", "bottomSheetBehavior is $it")
                it.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        when (newState) {
                            BottomSheetBehavior.STATE_COLLAPSED, BottomSheetBehavior.STATE_EXPANDED -> {
                                binding.bottomSheetTracks.isVisible = false
                            }
                            else -> {
                                binding.bottomSheetTracks.isVisible = true
                            }
                        }
                        binding.overlay.isVisible = !(newState == BottomSheetBehavior.STATE_HIDDEN)
                    }
                    override fun onSlide(bottomSheet: View, slideOffset: Float) { }
                })
            }

        binding.moreIcon.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.shareIcon.setOnClickListener {
            sendPlaylistMessage()
        }

        binding.share.setOnClickListener {
            sendPlaylistMessage()
        }

        binding.delete.setOnClickListener {
            val deletePlaylistDialog = createConfirmPlaylistRemovalDialog()
            deletePlaylistDialog.show()
        }

        binding.edit.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistFragment_to_playlistEditorFragment,
                createArgs(playlistId)
            )
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d("LifecycleFragment","onResume || PlaylistFragment")
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onDestroyView() {
        Log.d("LifecycleFragment","onDestroyView || PlaylistFragment")
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
            val deleteTrackDialog = createConfirmTrackRemovalDialog(trackRepresentation)
            deleteTrackDialog.show()
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

    private fun createConfirmPlaylistRemovalDialog(): MaterialAlertDialogBuilder {
        val dialogListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    viewModel.deletePlaylist()
                    findNavController().navigateUp()
                }
            }
        }
        return MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.delete_playlist))
            .setMessage(resources.getString(R.string.do_you_want_to_delete_playlist))
            .setPositiveButton(resources.getString(R.string.yes), dialogListener)
            .setNegativeButton(resources.getString(R.string.no), dialogListener)
    }


    private fun render(playlistInfo: PlaylistInfo) {

        val playlist = playlistInfo.playlist

        Glide.with(this)
            .load(playlist.coverUri)
            .signature(ObjectKey(playlistInfo.playlist.id))
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
                playlistDescription.text = playlist.description
                playlistDescription.isVisible = true
            }
            playlistDuration.text = resources.getQuantityString(R.plurals.minute_plurals, playlistInfo.durationInMinutes, playlistInfo.durationInMinutes)
            playlistSize.text = resources.getQuantityString(R.plurals.track_plurals, playlist.size, playlist.size)
        }

        playlistTracks.clear()
        playlistTracks.addAll(playlistInfo.tracks.map { it.changeCoverResolutionTo60(it) })
        adapter?.notifyDataSetChanged()

        binding.thereIsNoTracksInPlaylist.isVisible = playlistInfo.tracks.isEmpty()
    }

    private fun renderPlaylistInfoForBottomSheet(playlistInfo: PlaylistInfo) {

        val playlist = playlistInfo.playlist

        binding.playlistBriefInfo.apply {
            Glide.with(this@PlaylistFragment)
                .load(playlist.coverUri)
                .signature(ObjectKey(playlistInfo.playlist.id))
                .apply(
                    RequestOptions().placeholder(R.drawable.track_icon_mock)
                )
                .transform(
                    CenterCrop(),
                    RoundedCorners(resources.getDimensionPixelSize(R.dimen.rounded_corners_for_big_covers))
                )
                .into(cover)
            title.text = playlist.title
            size.text = resources.getQuantityString(R.plurals.track_plurals, playlist.size, playlist.size)
        }
    }

    private fun formMessage(playlistInfo: PlaylistInfo): String {

        val playlist = playlistInfo.playlist

        val message = StringBuilder().apply {
            appendLine(playlist.title)
            if (playlist.description?.isNotBlank() == true) appendLine(playlist.description)
            appendLine(resources.getQuantityString(R.plurals.track_plurals, playlist.size, playlist.size))
        }

        playlistInfo.tracks.forEach {
            message.appendLine(
                resources.getString(R.string.track_form_for_message)
                    .format(playlistInfo.tracks.indexOf(it) + 1, it.artistName, it.trackName, it.trackTime)
            )
        }

        return message.toString()
    }

    private fun sendPlaylistMessage() {
        viewModel.liveDataPlaylist.value?.let {
            if (it.tracks.isEmpty()) {
                Toast.makeText(requireContext(), "В этом плейлисте нет списка треков, которым можно поделиться", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.sharePlaylist(
                    formMessage(it)
                )
            }
        }
    }

    companion object {

        const val PLAYLIST_ID = "PLAYLIST_ID"

        fun createArgs(playlistId: Int) = bundleOf(PLAYLIST_ID to playlistId)
    }

    //-------  УДАЛИТЬ КОД НИЖЕ ЧЕРТЫ  ------------------------------------------------------------
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("LifecycleFragment", "onAttach || PlaylistFragment")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifecycleFragment","onCreate || PlaylistFragment")
    }

    override fun onStart() {
        super.onStart()
        Log.d("LifecycleFragment","onStart || PlaylistFragment")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifecycleFragment","onPause || PlaylistFragment")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LifecycleFragment","onStop || PlaylistFragment")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LifecycleFragment","onDestroy || PlaylistFragment")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("LifecycleFragment","onDetach || PlaylistFragment")
    }

}