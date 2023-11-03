package com.example.playlistmaker.ui.mediateca.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediatecaBinding
import com.example.playlistmaker.ui.mediateca.fragments.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediatecaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediatecaBinding

    private lateinit var tabLayoutMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediatecaBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) {
            tab, position ->
            tab.text = when(position) {
                0 -> this.getString(R.string.favourite_tracks)
                1 -> this.getString(R.string.playlists)
                else -> ""
            }
        }
        tabLayoutMediator.attach()

        binding.backToMainScreen.setOnClickListener {
            finish()
        }
    }

}