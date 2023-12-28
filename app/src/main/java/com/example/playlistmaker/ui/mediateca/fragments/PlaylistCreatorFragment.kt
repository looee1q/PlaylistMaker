package com.example.playlistmaker.ui.mediateca.fragments

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
import androidx.core.graphics.drawable.toBitmap
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreateNewPlaylistBinding
import com.example.playlistmaker.roundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.FileOutputStream

class PlaylistCreatorFragment : Fragment() {

    private var _binding: FragmentCreateNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private val dialog by lazy { createConfirmPlaylistCreationDialog() }

    private var photoUriToSave: Uri? = null

    private val photoLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()) { photoUri ->
        if (photoUri != null) {
            Glide.with(this)
                .load(photoUri)
                .transform(CenterCrop(), roundedCorners(RADIUS_OF_PLAYLIST_COVER_CORNERS, resources))
                .into(binding.playlistCover)
            photoUriToSave = photoUri
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

        binding.playlistCover.setOnClickListener {
            photoLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.playlistNameTextInputEditText.doOnTextChanged { text, start, before, count ->
            binding.createNewPlaylistButton.isEnabled = text?.isNotBlank() == true
        }

        binding.createNewPlaylistButton.setOnClickListener {
            val playlistName = binding.playlistNameTextInputEditText.text.toString()
            Toast.makeText(requireContext(), "Плейлист $playlistName создан", Toast.LENGTH_SHORT).show()
            photoUriToSave?.let { saveCoverToExternalStorage(it) }
            findNavController().navigateUp()
        }

        binding.backArrowButton.setOnClickListener {
            dialog.show()
        }

        requireActivity().onBackPressedDispatcher.addCallback( object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dialog.show()
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
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(coverFile)
        BitmapFactory.decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
    }

    companion object {
        private const val RADIUS_OF_PLAYLIST_COVER_CORNERS: Float = 8f
    }

}

