package com.example.playlistmaker.ui.mediateca.playlists.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediatecaPlaylistsBinding
import com.example.playlistmaker.ui.mediateca.playlists.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediatecaPlaylistsFragment : Fragment() {

    private var _binding: FragmentMediatecaPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediatecaPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createNewPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediatecaFragment_to_playlistCreatorFragment
            )
        }

        viewModel.showPlaylists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() : MediatecaPlaylistsFragment {
            return MediatecaPlaylistsFragment()
        }
    }

}