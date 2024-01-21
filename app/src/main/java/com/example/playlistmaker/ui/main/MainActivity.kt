package com.example.playlistmaker.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Lifecycle","onCreate || MainActivity")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.root_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController.also {
            it.addOnDestinationChangedListener { controller, destination, arguments ->
                binding.bottomNavigationView.isVisible = when (destination.id) {
                    R.id.playlistCreatorFragment, R.id.playlistFragment -> false
                    else -> true
                }
            }
        }

        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        Log.d("Lifecycle","onStart || MainActivity")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Lifecycle","onResume || MainActivity")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Lifecycle","onPause || MainActivity")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Lifecycle","onStop || MainActivity")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Lifecycle","onDestroy || MainActivity")
    }
}