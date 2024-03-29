package com.example.playlistmaker.library.playlists.newPlaylist.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.library.favorites.domain.models.Playlist
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.utils.BottomNavigationListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.util.*


class NewPlaylistFragment : Fragment() {
    private val viewModel by viewModel<NewPlaylistViewModel>()
    private val binding get() = _binding!!
    private var _binding: FragmentNewPlaylistBinding? = null
    private var currentPlaylist: Playlist? = null
    private var coverUri: Uri? = null

    private var bottomNavigationListener: BottomNavigationListener? = null

    private var coverPath = ""
    private var backPressedOnce = false

    private lateinit var titleTextWatcher: TextWatcher
    private lateinit var descriptionTextWatcher: TextWatcher

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        if (arguments?.containsKey(PLAYLIST_ID) == true) {
            val currentPlaylistId = arguments?.getLong(PLAYLIST_ID)
            if (currentPlaylistId != null)
                viewModel.getPlaylistInfo(currentPlaylistId)
        }
        viewModel.observePlaylist().observe(viewLifecycleOwner) { playlist ->
            currentPlaylist = playlist
            setPlaylistInfo(playlist)
        }

        initCoverPicker()
        initTextWatchers()
        setListeners()
        setSaveButtonVisibility(null)
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.newPlaylist_question_title))
            .setMessage(getString(R.string.newPlaylist_question_message))
            .setNeutralButton(getString(R.string.newPlaylist_question_cancel_button)) { dialog, which ->
            }
            .setPositiveButton(getString(R.string.newPlaylist_question_save_button)) { dialog, which ->
                navigateOut()
                coverUri = null
                binding.name.text = null
                binding.description.text = null
            }

        bottomNavigationListener?.setBottomNavigationVisibility(false)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        titleTextWatcher.let { binding.name.removeTextChangedListener(it) }
        descriptionTextWatcher.let { binding.description.removeTextChangedListener(it) }
        _binding = null
    }

    private fun setSaveButtonVisibility(name: CharSequence?) {
        binding.save.isEnabled = !name.isNullOrEmpty()
    }

    private fun setListeners() {
        binding.back.setOnClickListener {
            checkNavigateOut()
        }

        binding.save.setOnClickListener {
            if (binding.name.text.toString().isNotEmpty()) {
                if (currentPlaylist != null)
                    updatePlaylist()
                else
                    createPlaylist()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (coverUri != null || binding.name.text.toString()
                    .isNotEmpty() || binding.description.text.toString().isNotEmpty()
            ) {
                if (backPressedOnce) {
                    requireActivity().finish()
                } else {
                    confirmDialog.show()
                }
            } else {
                val currentActivity = activity
                if (currentActivity is PlayerActivity) {
                    backPressedOnce = false
                    (activity as PlayerActivity).closeNewPlaylistFragment()
                } else {
                    checkNavigateOut()
                }
            }
        }
    }


    private fun initCoverPicker() {
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    coverUri = uri
                    binding.cover.setImageURI(uri)
                }
            }

        binding.cover.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri?, playlistName: String): String {
        if (uri == null)
            return ""

        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            getString(R.string.newPlaylist_playlist_covers_folder)
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, UUID.randomUUID().toString() + ".jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.toString()
    }

    private fun initTextWatchers() {
        titleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setSaveButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                setEditTextColors(binding.nameLayout, s)
            }
        }

        binding.name.addTextChangedListener(titleTextWatcher)

        descriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // empty
            }

            override fun afterTextChanged(s: Editable?) {
                setEditTextColors(binding.descriptionLayout, s)
            }
        }

        binding.description.addTextChangedListener(descriptionTextWatcher)
    }

    private fun setEditTextColors(textInputLayout: TextInputLayout, text: CharSequence?) {
        val editTextColor = if (text.toString().isEmpty()) {
            ResourcesCompat.getColorStateList(
                resources,
                R.color.edit_text_selector,
                requireContext().theme
            )
        } else {
            ResourcesCompat.getColorStateList(
                resources,
                R.color.edit_text_selector_filled,
                requireContext().theme
            )
        }

        if (editTextColor != null) {
            textInputLayout.setBoxStrokeColorStateList(editTextColor)
            textInputLayout.hintTextColor = editTextColor
            textInputLayout.defaultHintTextColor = editTextColor
        }
    }

    private fun navigateOut() {
        val currentTrackId = arguments?.getString(CURRENT_TRACK_ID)
        if (currentTrackId.isNullOrEmpty()) {
            findNavController().navigateUp()
        } else {
            (activity as PlayerActivity).closeNewPlaylistFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomNavigationListener) {
            bottomNavigationListener = context
        }
    }


    override fun onDetach() {
        super.onDetach()
        bottomNavigationListener = null
    }


    private fun checkNavigateOut() {
        if (currentPlaylist != null)
            findNavController().navigateUp()
        else {
            if (coverUri != null || binding.name.text.toString()
                    .isNotEmpty() || binding.description.text.toString().isNotEmpty()
            )
                confirmDialog.show()
            else
                navigateOut()
        }
    }

    private fun setPlaylistInfo(playlist: Playlist) {
        binding.name.setText(playlist.name)
        binding.description.setText(playlist.description)
        Glide.with(requireContext())
            .load(playlist.coverPath)
            .centerCrop()
            .placeholder(R.drawable.medialib_cover_placeholder)
            .into(binding.cover)
        binding.save.text = getString(R.string.save)
        binding.headerText.text = getString(R.string.edit)
        coverPath = playlist.coverPath
    }

    private fun updatePlaylist() {
        if (coverUri != null)
            coverPath = saveImageToPrivateStorage(coverUri, binding.name.text.toString())
        viewModel.updatePlaylist(
            currentPlaylist?.id,
            binding.name.text.toString(),
            binding.description.text.toString(),
            coverPath
        )
        checkNavigateOut()
    }

    private fun createPlaylist() {
        coverPath = saveImageToPrivateStorage(coverUri, binding.name.text.toString())
        viewModel.createPlaylist(
            binding.name.text.toString(),
            binding.description.text.toString(),
            coverPath
        )
        Toast.makeText(
            requireContext(),
            "Плейлист " + binding.name.text.toString() + " создан",
            Toast.LENGTH_LONG
        ).show()
        navigateOut()
    }

    companion object {
        private const val PLAYLIST_ID = "playlist_id"
        const val CURRENT_TRACK_ID = "CURRENT_TRACK_ID"

        fun createArgs(playlistId: Long): Bundle =
            bundleOf(PLAYLIST_ID to playlistId)

    }
}