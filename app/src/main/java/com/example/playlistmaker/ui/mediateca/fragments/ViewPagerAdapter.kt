package com.example.playlistmaker.ui.mediateca.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

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