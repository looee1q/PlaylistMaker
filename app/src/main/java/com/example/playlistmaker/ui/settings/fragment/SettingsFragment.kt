package com.example.playlistmaker.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.domain.settings.model.Theme
import com.example.playlistmaker.ui.settings.view_model.settings.SettingsViewModel
import com.example.playlistmaker.ui.settings.view_model.sharing.SharingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val settingsViewModel: SettingsViewModel by viewModel()

    private val sharingViewModel: SharingViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel.liveData.observe(this) {
            binding.nightModeSwitch.isChecked = when (it) {
                is Theme.DarkTheme -> true
                is Theme.DayTheme -> false
            }
        }

        settingsViewModel.getLastSavedTheme()

        binding.nightModeSwitch.setOnCheckedChangeListener { switcher, isChecked ->
            val theme = if (isChecked) Theme.DarkTheme() else Theme.DayTheme()
            settingsViewModel.saveAndSetNewTheme(theme)

        }

        binding.shareAppIcon.setOnClickListener {
            sharingViewModel.shareApp()
        }

        binding.writeToTheSupportIcon.setOnClickListener {
            sharingViewModel.writeToSupport()
        }

        binding.userAgreementIcon.setOnClickListener {
            sharingViewModel.getUserAgreement()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}