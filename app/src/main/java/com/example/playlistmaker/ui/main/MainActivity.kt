package com.example.playlistmaker.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.root_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

/*        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.bottomNavigationView.isVisible = when (destination.id) {
                R.id.mediatecaFragment,
                R.id.settingsFragment,
                R.id.searchFragment -> true
                else -> false
            }
        }*/

        binding.bottomNavigationView.setupWithNavController(navController)
    }
}