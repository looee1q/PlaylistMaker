package com.example.playlistmaker.ui.mediateca.favorites.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediatecaFavouritesBinding
import com.example.playlistmaker.ui.mediateca.model.Status
import com.example.playlistmaker.ui.mediateca.favorites.view_model.FavouritesViewModel
import com.example.playlistmaker.ui.models.TrackRepresentation
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import com.example.playlistmaker.ui.search.fragment.TrackAdapter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediatecaFavouritesFragment : Fragment() {

    private var _binding: FragmentMediatecaFavouritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavouritesViewModel by viewModel()

    private var adapter: TrackAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("LifecycleFragment", "onCreateView || MediatecaFavoritesFragment")
        _binding = FragmentMediatecaFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("LifecycleFragment", "onViewCreated || MediatecaFavoritesFragment")

        adapter = TrackAdapter(viewModel.liveDataFavoriteTracks.value!!)
        adapter?.listener = createAdapterListener()
        binding.favoriteTracksRecyclerView.adapter = adapter
        binding.favoriteTracksRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, true
        ).also { it.stackFromEnd = true }

        viewModel.liveDataFavoritesStatus.observe(viewLifecycleOwner) {
            Log.d("MediatecaFavoritesFragment", "Я наблюдаю за liveDataFavoritesStatus!")
            render(it)
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d("LifecycleFragment", "onResume || MediatecaFavoritesFragment")
        viewModel.showFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("LifecycleFragment", "onDestroyView || MediatecaFavoritesFragment")
        _binding = null
        adapter = null
    }

    private fun createAdapterListener(): (TrackRepresentation) -> Unit {
        return { track: TrackRepresentation ->
            if (viewModel.liveDataIsClickOnTrackAllowed.value!!) {
                viewModel.clickOnTrackDebounce()

                findNavController().navigate(
                    R.id.action_mediatecaFragment_to_playerActivity,
                    PlayerActivity.createArgs(Json.encodeToString(track))
                )
            }
        }
    }

    private fun render(status: Status<List<TrackRepresentation>>?) {
        when(status) {
            is Status.Content -> {
                with(binding) {
                    emptyMediatecaImage.isVisible = false
                    emptyMediatecaMessage.isVisible = false
                    favoriteTracksRecyclerView.isVisible = true
                }
                adapter?.notifyDataSetChanged()
            }
            is Status.Empty, null -> {
                with(binding) {
                    emptyMediatecaImage.isVisible = true
                    emptyMediatecaMessage.isVisible = true
                    favoriteTracksRecyclerView.isVisible = false
                }
            }
        }
    }

    companion object {
        fun newInstance() : MediatecaFavouritesFragment {
            return MediatecaFavouritesFragment()
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("LifecycleFragment", "onAttach || MediatecaFavoritesFragment")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifecycleFragment", "onCreate || MediatecaFavoritesFragment")
    }

    override fun onStart() {
        super.onStart()
        Log.d("LifecycleFragment", "onStart || MediatecaFavoritesFragment")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifecycleFragment", "onPause || MediatecaFavoritesFragment")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LifecycleFragment", "onStop || MediatecaFavoritesFragment")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LifecycleFragment", "onDestroy || MediatecaFavoritesFragment")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("LifecycleFragment", "onDetach || MediatecaFavoritesFragment")
    }

}