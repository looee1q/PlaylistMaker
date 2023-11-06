package com.example.playlistmaker.ui.mediateca.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentMediatecaFavouritesBinding
import com.example.playlistmaker.ui.mediateca.view_models.FavouritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediatecaFavouritesFragment : Fragment() {

    private var _binding: FragmentMediatecaFavouritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavouritesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediatecaFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() : MediatecaFavouritesFragment {
            return MediatecaFavouritesFragment()
        }
    }
}