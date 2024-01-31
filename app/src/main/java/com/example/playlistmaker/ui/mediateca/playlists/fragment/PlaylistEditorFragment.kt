package com.example.playlistmaker.ui.mediateca.playlists.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreateNewPlaylistBinding
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.ui.mediateca.playlists.view_model.PlaylistEditorViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistEditorFragment : Fragment() {

    private var _binding: FragmentCreateNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private var playlistId: Int = -1

    private val viewModel by viewModel<PlaylistEditorViewModel> { parametersOf(playlistId) }

    private val photoLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()) { photoUri ->
        if (photoUri != null) {
            Glide.with(this)
                .load(photoUri)
                .transform(
                    CenterCrop(),
                    RoundedCorners(requireActivity().resources.getDimensionPixelSize(R.dimen.rounded_corners_for_big_covers))
                )
                .into(binding.playlistCover)
            viewModel.setNewPlaylistUri(photoUri)
        } else {
            Log.d("PhotoLauncher", "No cover selected")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("LifecycleFragment", "onCreateView || PlaylistEditorFragment")
        _binding = FragmentCreateNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("LifecycleFragment", "onViewCreated || PlaylistEditorFragment")
        playlistId = arguments?.getInt(PlaylistFragment.PLAYLIST_ID)!!

        renderUI()

        binding.playlistCover.setOnClickListener {
            photoLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.playlistNameTextInputEditText.doOnTextChanged { titleAfterEditing, start, before, count ->
            binding.createNewPlaylistButton.isEnabled = titleAfterEditing?.isNotBlank() == true
        }

        binding.createNewPlaylistButton.setOnClickListener {
            saveTitleIfUpdated()
            saveDescriptionIfUpdated()

            viewModel.saveCoverToExternalStorage()
            viewModel.updatePlaylist()

            findNavController().navigateUp()
        }

        binding.backArrowButton.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d("LifecycleFragment","onResume || PlaylistEditorFragment")
        viewModel.liveDataPlaylist.observe(viewLifecycleOwner) {
            Log.d("PlaylistEditorFragment","observe liveDataPlaylist in PlaylistEditorFragment")
            renderPlaylistInfo(it)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifecycleFragment","onPause || PlaylistEditorFragment")
        viewModel.liveDataPlaylist.removeObservers(viewLifecycleOwner)
        saveTitleIfUpdated()
        saveDescriptionIfUpdated()
    }

    override fun onDestroyView() {
        Log.d("LifecycleFragment", "onDestroyView || PlaylistEditorFragment")
        super.onDestroyView()
        _binding = null
    }

    private fun saveTitleIfUpdated() {
        val newTitle = binding.playlistNameTextInputEditText.text.toString()
        if (newTitle != viewModel.liveDataPlaylist.value?.title) {
            Log.d("PlaylistEditorFragment","Сейчас поменяю title на $newTitle")
            viewModel.setNewPlaylistTitle(newTitle)
        }
    }

    private fun saveDescriptionIfUpdated() {
        val newDescription = binding.playlistDescriptionTextInputEditText.text.toString()
        if (newDescription != viewModel.liveDataPlaylist.value?.description) {
            Log.d("PlaylistEditorFragment","Сейчас поменяю description на $newDescription")
            viewModel.setNewPlaylistDescription(newDescription)
        }
    }

    private fun renderUI() {
        binding.newPlaylist.text = resources.getString(R.string.edit)
        binding.createNewPlaylistButton.text = resources.getString(R.string.save)
    }

    private fun renderPlaylistInfo(playlist: Playlist) {

        Glide.with(this)
            .load(playlist.coverUri)
            .apply(
                RequestOptions().placeholder(R.drawable.track_icon_mock)
            )
            .transform(
                CenterCrop(),
                RoundedCorners(requireActivity().resources.getDimensionPixelSize(R.dimen.rounded_corners_for_big_covers))
            )
            .into(binding.playlistCover)

        binding.playlistNameTextInputEditText.setText(playlist.title)

        playlist.description?.let {
            binding.playlistDescriptionTextInputEditText.setText(playlist.description)
        }
    }

    //-------  УДАЛИТЬ КОД НИЖЕ ЧЕРТЫ  ------------------------------------------------------------

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("LifecycleFragment", "onAttach || PlaylistEditorFragment")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifecycleFragment","onCreate || PlaylistEditorFragment")
    }

    override fun onStart() {
        super.onStart()
        Log.d("LifecycleFragment","onStart || PlaylistEditorFragment")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LifecycleFragment","onStop || PlaylistEditorFragment")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LifecycleFragment","onDestroy || PlaylistEditorFragment")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("LifecycleFragment","onDetach || PlaylistEditorFragment")
    }

}