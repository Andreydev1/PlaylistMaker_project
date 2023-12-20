package com.example.playlistmaker.library.playlists.createdPlaylist.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatedPlaylistBinding
import com.example.playlistmaker.library.favorites.domain.models.Playlist
import com.example.playlistmaker.library.playlists.createdPlaylist.domain.models.CreatedPlaylistState
import com.example.playlistmaker.library.playlists.newPlaylist.ui.NewPlaylistFragment
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.utils.debounceDelay
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatedPlaylistFragment : Fragment() {
    private var _binding: FragmentCreatedPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<CreatedPlaylistViewModel>()

    private lateinit var tracksBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private var isShareButtonEnabled = true
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())


    private val tracksAdapter = TrackAdapter("60")
    private lateinit var confirmDialog: MaterialAlertDialogBuilder
    private lateinit var createdPlaylist: Playlist

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatedPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPlaylistInfo(requireArguments().getLong(PLAYLIST_ID))

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        initBottomSheet()

        binding.playlistBack.setOnClickListener { goBack() }


        binding.playlistMenu.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }


        binding.playlistMenuEdit.setOnClickListener { editPlaylist() }
        binding.playlistMenuDelete.setOnClickListener {
            confirmDialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.created_playlist_delete_question_title))
                .setMessage(
                    getString(
                        R.string.created_playlist_delete_question_message,
                        createdPlaylist.name
                    )
                )
                .setNeutralButton(getString(R.string.no)) { dialog, which ->
                }.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                    viewModel.deletePlaylist(createdPlaylist)
                }
            confirmDialog.show()
        }
    }

    private fun editPlaylist() {
        createdPlaylist.id?.let {
            findNavController()
                .navigate(
                    R.id.action_createdPlaylistFragment_to_newPlaylistFragment,
                    NewPlaylistFragment.createArgs(it)
                )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBottomSheet() {
        binding.playlistTracksRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.playlistTracksRecyclerView.adapter = tracksAdapter

        tracksBottomSheetBehavior = BottomSheetBehavior.from(binding.playlistTracksBottomSheet)
        tracksBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        tracksBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        tracksAdapter.onItemClick = { track ->
            onTrackClickDebounce(track)
        }


        binding.playlistShare.setOnClickListener {
            if (isShareButtonEnabled) {
                viewModel.sharePlaylist(createdPlaylist, tracksAdapter.tracks)
                isShareButtonEnabled = false
            }
            coroutineScope.launch {
                delay(1000)
                isShareButtonEnabled = true
            }
        }

        binding.playlistMenuShare.setOnClickListener {
            if (isShareButtonEnabled) {
                viewModel.sharePlaylist(createdPlaylist, tracksAdapter.tracks)
                isShareButtonEnabled = false
            }
            coroutineScope.launch {
                delay(1000)
                isShareButtonEnabled = true
            }
        }

        tracksAdapter.onLongItemClick = { track ->
            confirmDialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.created_playlist_question_title))
                .setMessage(getString(R.string.created_playlist_question_message))
                .setNeutralButton(getString(R.string.created_playlist_question_cancel)) { dialog, which ->
                }
                .setPositiveButton(getString(R.string.created_playlist_question_delete)) { dialog, which ->
                    viewModel.deleteTrack(track, requireArguments().getLong(PLAYLIST_ID))
                }
            confirmDialog.show()
            true
        }

        onTrackClickDebounce =
            debounceDelay<Track>(CLICK_DEBOUNCE_DELAY, lifecycleScope, false) { track ->
                val player = Intent(requireContext(), PlayerActivity::class.java)
                player.putExtra("track", track)
                startActivity(player)
            }


        binding.playlistTracksRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.playlistTracksRecyclerView.adapter = tracksAdapter

        menuBottomSheetBehavior = BottomSheetBehavior.from(binding.playlistMenuBottomSheet)
        menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN



        menuBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        fillPlaylistMenu()
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun render(state: CreatedPlaylistState) {
        when (state) {
            is CreatedPlaylistState.Success -> showSuccess(state.playlist, state.tracks)
            is CreatedPlaylistState.Empty -> showEmpty()
            is CreatedPlaylistState.Share -> showShareMenu(state.intent)
            is CreatedPlaylistState.Deleted -> goBack()
        }
    }

    private fun goBack() {
        findNavController().navigateUp()
    }

    private fun showShareMenu(intent: Intent) {
        startActivity(intent)
    }

    private fun showEmpty() {
        Toast.makeText(
            requireContext(),
            getString(R.string.created_playlist_share_empty),
            Toast.LENGTH_LONG
        ).show()
    }


    private fun showSuccess(playlist: Playlist, tracks: List<Track>) {
        createdPlaylist = playlist
        binding.playlistName.text = playlist.name
        binding.playlistDescription.text = playlist.description

        val playlistDuration = viewModel.countPlaylistDuration(tracks).toInt()
        binding.playlistDuration.text = resources.getQuantityString(
            R.plurals.minutes_plurals,
            playlistDuration,
            playlistDuration
        )
        binding.playlistSize.text =
            resources.getQuantityString(R.plurals.tracks_plurals, playlist.size, playlist.size)
        Glide.with(requireContext())
            .load(playlist.coverPath)
            .centerCrop()
            .placeholder(R.drawable.medialib_cover_placeholder)
            .into(binding.playlistCover)

        if (playlist.description.isEmpty())
            binding.playlistDescription.text = resources.getText(R.string.empty_description)
        else
            binding.playlistDescription.visibility = View.VISIBLE

        if (tracks.isEmpty())
            Toast.makeText(
                requireContext(),
                getString(R.string.created_playlist_empty),
                Toast.LENGTH_LONG
            ).show()

        tracksAdapter.tracks.clear()
        tracksAdapter.tracks.addAll(tracks)
        tracksAdapter.notifyDataSetChanged()
    }

    private fun fillPlaylistMenu() {
        binding.menuPlaylistName.text = createdPlaylist.name
        binding.playlistSize.text = resources.getQuantityString(
            R.plurals.tracks_plurals,
            createdPlaylist.size,
            createdPlaylist.size
        )
        Glide.with(requireContext())
            .load(createdPlaylist.coverPath)
            .transform(RoundedCorners(8))
            .centerCrop()
            .placeholder(R.drawable.medialib_cover_placeholder)
            .into(binding.menuPlaylistCover)
    }


    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val PLAYLIST_ID = "playlist_id"

        fun createArgs(playlistId: Long): Bundle =
            bundleOf(PLAYLIST_ID to playlistId)
    }
}
