package com.example.playlistmaker.ui.mediateca.root_fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.mediateca.favorites.fragment.MediatecaFavouritesFragment
import com.example.playlistmaker.ui.mediateca.playlists.fragment.MediatecaPlaylistsFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = FRAGMENT_QUANTITY

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> MediatecaFavouritesFragment.newInstance()
            1 -> MediatecaPlaylistsFragment.newInstance()
            else -> Fragment()
        }
    }

    companion object {
        private const val FRAGMENT_QUANTITY = 2
    }
}