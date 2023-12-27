package com.example.playlistmaker.ui.mediateca.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.databinding.FragmentCreateNewPlaylistBinding
import com.example.playlistmaker.roundedCorners

class PlaylistCreatorFragment : Fragment() {

    private var _binding: FragmentCreateNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private val photoLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()) { photoUri ->
        if (photoUri != null) {
            Glide.with(this)
                .load(photoUri)
                .transform(CenterCrop(), roundedCorners(RADIUS_OF_PLAYLIST_COVER_CORNERS, resources))
                .into(binding.playlistCover)
        } else {
            Log.d("PhotoLauncher", "No cover selected")
        }
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

        binding.createNewPlaylistButton.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {

        private const val RADIUS_OF_PLAYLIST_COVER_CORNERS: Float = 8f
    }

}