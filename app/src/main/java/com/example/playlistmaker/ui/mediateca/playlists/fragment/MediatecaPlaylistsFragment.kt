package com.example.playlistmaker.ui.mediateca.playlists.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediatecaPlaylistsBinding
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.ui.mediateca.model.Status
import com.example.playlistmaker.ui.mediateca.playlists.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediatecaPlaylistsFragment : Fragment() {

    private var _binding: FragmentMediatecaPlaylistsBinding? = null
    private val binding get() = _binding!!

    private var _adapter: PlaylistsAdapter? = null
    private val adapter get() = _adapter!!

    private val viewModel: PlaylistsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("LifecycleFragment", "onCreateView || MediatecaPlaylistsFragment")
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

        _adapter = PlaylistsAdapter(viewModel.liveDataPlaylists.value!!)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.liveDataPlaylistsStatus.observe(viewLifecycleOwner) {
            render(it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("LifecycleFragment", "onDestroyView || MediatecaPlaylistsFragment")
        _binding = null
        _adapter = null
    }

    private fun render(status: Status<List<Playlist>>?) {
        when(status) {
            is Status.Content -> {
                with(binding) {
                    playlistsNotFoundImage.isVisible = false
                    playlistsNotFoundMessage.isVisible = false
                    recyclerView.isVisible = true
                }
                //adapter?.notifyDataSetChanged()
            }
            is Status.Empty, null -> {
                with(binding) {
                    playlistsNotFoundImage.isVisible = true
                    playlistsNotFoundMessage.isVisible = true
                    recyclerView.isVisible = false
                }
            }
        }
    }

    companion object {
        fun newInstance() : MediatecaPlaylistsFragment {
            return MediatecaPlaylistsFragment()
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("LifecycleFragment", "onAttach || MediatecaPlaylistsFragment")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifecycleFragment", "onCreate || MediatecaPlaylistsFragment")
    }

    override fun onStart() {
        super.onStart()
        Log.d("LifecycleFragment", "onStart || MediatecaPlaylistsFragment")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifecycleFragment", "onResume || MediatecaPlaylistsFragment")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifecycleFragment", "onPause || MediatecaPlaylistsFragment")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LifecycleFragment", "onStop || MediatecaPlaylistsFragment")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LifecycleFragment", "onDestroy || MediatecaPlaylistsFragment")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("LifecycleFragment", "onDetach || MediatecaPlaylistsFragment")
    }

}