package com.example.playlistmaker.ui.mediateca.playlists.fragment

import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreateNewPlaylistBinding
import com.example.playlistmaker.roundedCorners
import com.example.playlistmaker.ui.mediateca.playlists.view_model.PlaylistCreatorViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class PlaylistCreatorFragment : Fragment() {

    private var _binding: FragmentCreateNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistCreatorViewModel by viewModel()

    private val dialog by lazy { createConfirmPlaylistCreationDialog() }

    private val photoLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()) { photoUri ->
        if (photoUri != null) {
            Glide.with(this)
                .load(photoUri)
                .transform(CenterCrop(), roundedCorners(RADIUS_OF_PLAYLIST_COVER_CORNERS, resources))
                .into(binding.playlistCover)
            viewModel.setUri(photoUri)
        } else {
            Log.d("PhotoLauncher", "No cover selected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("PlaylistCreatorFragment","onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveDataPlaylistUri.value?.let {
            binding.playlistCover.setImageURI(it)
        }

        binding.playlistCover.setOnClickListener {
            photoLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.playlistNameTextInputEditText.doOnTextChanged { text, start, before, count ->
            binding.createNewPlaylistButton.isEnabled = text?.isNotBlank() == true
        }

        binding.createNewPlaylistButton.setOnClickListener {
            val playlistName = binding.playlistNameTextInputEditText.text.toString()
            Toast.makeText(requireContext(), resources.getString(R.string.playlist_created).format(playlistName), Toast.LENGTH_SHORT).show()
            viewModel.liveDataPlaylistUri.value?.let { saveCoverToExternalStorage(it) }

            viewModel.createPlaylist(
                title = binding.playlistNameTextInputEditText.text.toString(),
                description = binding.playlistDescriptionTextInputEditText.text.toString(),
                coverUri = viewModel.liveDataPlaylistUri.value
            )

            findNavController().navigateUp()
        }

        binding.backArrowButton.setOnClickListener {
            closeFragment()
        }

        requireActivity().onBackPressedDispatcher.addCallback( object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                closeFragment()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("PlaylistCreatorFragment","onDestroy")
    }

    private fun createConfirmPlaylistCreationDialog(): MaterialAlertDialogBuilder {
        val dialogListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    findNavController().navigateUp()
                }
                DialogInterface.BUTTON_NEUTRAL -> null
            }
        }
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.finish_playlist_creation))
            .setMessage(resources.getString(R.string.all_unsaved_data_will_be_lost))
            .setPositiveButton(resources.getString(R.string.finish), dialogListener)
            .setNeutralButton(resources.getString(R.string.cancel), dialogListener)
    }

    private fun saveCoverToExternalStorage(uri: Uri) {
        val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "PlaylistsCovers")
        if (!filePath.exists()) filePath.mkdirs()
        val coverFile = File(filePath, "cover_${filePath.listFiles().size + 1}.jpg")
        viewModel.setUri(coverFile.toUri())
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(coverFile)
        BitmapFactory.decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
    }

    private fun closeFragment() {
        if (binding.playlistDescriptionTextInputEditText.text?.isNotBlank() == true
            || binding.playlistNameTextInputEditText.text?.isNotBlank() == true
            || viewModel.liveDataPlaylistUri.value != null
        ) {
            dialog.show()
        } else {
            findNavController().navigateUp()
        }
    }

    companion object {
        private const val RADIUS_OF_PLAYLIST_COVER_CORNERS: Float = 8f
    }

}

