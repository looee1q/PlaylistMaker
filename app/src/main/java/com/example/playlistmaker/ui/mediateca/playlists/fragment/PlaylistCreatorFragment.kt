package com.example.playlistmaker.ui.mediateca.playlists.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreateNewPlaylistBinding
import com.example.playlistmaker.ui.mediateca.playlists.view_model.PlaylistCreatorViewModel
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistCreatorFragment : Fragment() {

    private var _binding: FragmentCreateNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistCreatorViewModel by viewModel()

    private val playerActivityViewModel by activityViewModel<PlayerViewModel>()

    private val dialog by lazy { createConfirmPlaylistCreationDialog() }

    private var isTransactionFromActivity: Boolean? = null

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Log.d("BACK_PRESSED_DISPATCHER", "FROM_PLAYLIST_CREATOR_FRAGMENT")
            closeFragment()
        }
    }

    private val photoLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()) { photoUri ->
        if (photoUri != null) {
            Glide.with(this)
                .load(photoUri)
                .transform(
                    CenterCrop(),
                    RoundedCorners(requireActivity().resources.getDimensionPixelSize(R.dimen.rounded_corners_for_big_covers))
                )
                .into(binding.playlistCover)
            viewModel.setUri(photoUri)
        } else {
            Log.d("PhotoLauncher", "No cover selected")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("LifecycleFragment","onCreateView || PlaylistCreatorFragment")
        _binding = FragmentCreateNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("LifecycleFragment","onViewCreated || PlaylistCreatorFragment")

        isTransactionFromActivity = arguments?.getBoolean(PlayerActivity.FROM_PLAYER_ACTIVITY)

        viewModel.liveDataPlaylistUri.value?.let {
            binding.playlistCover.setImageURI(it)
        }

        binding.playlistCover.setOnClickListener {
            photoLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.playlistNameTextInputEditText.doOnTextChanged { text, start, before, count ->
            binding.createNewPlaylistButton.isEnabled = text?.isNotBlank() == true
        }

        binding.createNewPlaylistButton.setOnClickListener {
            val playlistName = binding.playlistNameTextInputEditText.text.toString()
            Toast.makeText(requireContext(), resources.getString(R.string.playlist_created).format(playlistName), Toast.LENGTH_SHORT).show()

            viewModel.createPlaylist(
                title = binding.playlistNameTextInputEditText.text.toString(),
                description = binding.playlistDescriptionTextInputEditText.text.toString()
            )

            goBackNavigation()
        }

        binding.backArrowButton.setOnClickListener {
            closeFragment()
        }

        activity?.onBackPressedDispatcher?.addCallback(onBackPressedCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("LifecycleFragment","onDestroyView || PlaylistCreatorFragment")
        onBackPressedCallback.remove()
        _binding = null

        if (isTransactionFromActivity == true) {      //Костыль!!! Удалить при рефакторинге на SingleActivity
            playerActivityViewModel.getPlaylists()
            (requireActivity() as? PlayerActivity)?.showPlaylists()
        }
    }

    private fun createConfirmPlaylistCreationDialog(): MaterialAlertDialogBuilder {
        val dialogListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    goBackNavigation()
                }
            }
        }
        return MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.finish_playlist_creation))
            .setMessage(resources.getString(R.string.all_unsaved_data_will_be_lost))
            .setPositiveButton(resources.getString(R.string.finish), dialogListener)
            .setNeutralButton(resources.getString(R.string.cancel), dialogListener)
    }

    private fun closeFragment() {
        if (binding.playlistDescriptionTextInputEditText.text?.isNotBlank() == true
            || binding.playlistNameTextInputEditText.text?.isNotBlank() == true
            || viewModel.liveDataPlaylistUri.value != null
        ) {
            dialog.show()
        } else {
            goBackNavigation()
        }
    }

    private fun goBackNavigation() {
        if (isTransactionFromActivity == true) {
            playerActivityViewModel.getPlaylists()
            parentFragmentManager.popBackStack()
            Log.d("THE_WAY_OUT", "FROM ACTIVITY")
        }
        else {
            findNavController().navigateUp()
            Log.d("THE_WAY_OUT", "FROM FRAGMENT")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("LifecycleFragment", "onAttach || PlaylistCreatorFragment")
        (requireActivity() as? PlayerActivity)?.showPlaylists(false)  //Костыль!!! Удалить при рефакторинге на SingleActivity
    }

    //-------  УДАЛИТЬ КОД НИЖЕ ЧЕРТЫ  ------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifecycleFragment","onCreate || PlaylistCreatorFragment")
    }

    override fun onStart() {
        super.onStart()
        Log.d("LifecycleFragment","onStart || PlaylistCreatorFragment")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifecycleFragment","onResume || PlaylistCreatorFragment")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifecycleFragment","onPause || PlaylistCreatorFragment")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LifecycleFragment","onStop || PlaylistCreatorFragment")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LifecycleFragment","onDestroy || PlaylistCreatorFragment")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("LifecycleFragment","onDetach || PlaylistCreatorFragment")
    }
}

