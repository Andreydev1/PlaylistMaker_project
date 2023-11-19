package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.library.favorites.domain.models.Playlist
import com.example.playlistmaker.library.favorites.domain.models.PlaylistState
import com.example.playlistmaker.library.playlists.newPlaylist.ui.NewPlaylistFragment
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.view_model.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.debounceDelay
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private val viewModel by viewModel<PlayerViewModel>()

    private lateinit var currentTrack: Track
    private val playlistsAdapter = PlaylistListAdapter()

    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentTrack = intent.getSerializableExtra("track") as Track
        viewModel.preparePlayer(currentTrack.previewUrl)
        binding.overlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewModel.observeFavoriteState().observe(this) {
            binding.playerLikeButton.setImageResource(if (it) R.drawable.medialib_liked_button else R.drawable.medialib_unlike_button)
        }
        viewModel.observeInsertTrackInPlaylist().observe(this) {
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            if (it.hideBottomSheet)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewModel.observePlayerState().observe(this) {
            binding.playerPlayButton.isEnabled = it.isPlayButtonEnabled
            binding.playerTimer.text = it.progress

            when (it) {
                is PlayerState.Playing -> binding.playerPlayButton.setImageResource(R.drawable.medialib_pause_button)
                else -> binding.playerPlayButton.setImageResource(R.drawable.medialib_play_button)
            }
        }
        viewModel.observeState().observe(this) {
            render(it)
        }
        setTrackInfo(currentTrack)
        setListeners()
        initBottomSheet()
    }

    fun hideBottomSheet(fragment: NewPlaylistFragment) {
        binding.scrollView.isVisible = true
        binding.playlistsBottomSheet.isVisible = true
        binding.overlay.isVisible = true

        binding.fragmentContainer.isVisible = false
        playlistsAdapter.playlists.clear()
        viewModel.getPlaylists()
    }

    private fun setListeners() {
        binding.playerBack.setOnClickListener {
            finish()
        }

        binding.playerPlayButton.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }
        binding.playerLikeButton.setOnClickListener {
            viewModel.onFavoriteClicked(currentTrack)
        }
        binding.newPlaylist.setOnClickListener {
            val newPlaylistFragment = NewPlaylistFragment()
            val argTrackId = Bundle()
            argTrackId.putString(CURRENT_TRACK_ID, currentTrack.id.toString())
            newPlaylistFragment.arguments = argTrackId

            binding.scrollView.isVisible = false
            binding.playlistsBottomSheet.isVisible = false
            binding.overlay.isVisible = false
            binding.fragmentContainer.isVisible = true

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, newPlaylistFragment)
                .commit()
        }
    }

    private fun setTrackInfo(track: Track) {
        binding.playerTrackName.text = track.trackName
        binding.playerArtistName.text = track.artistName
        binding.playerTrackDurationData.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime?.toInt())
        binding.playerTrackAlbumData.text = track.collectionName
        binding.playerTrackYearData.text = track.releaseDate?.substring(0, 4)
        binding.playerTrackGenreData.text = track.primaryGenreName
        binding.playerTrackCountryData.text = track.country
        Glide.with(applicationContext)
            .load(track.getCoverArtWork())
            .centerCrop()
            .transform((RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_rounded_corners))))
            .placeholder(R.drawable.medialib_cover_placeholder)
            .into(binding.playerAlbumCover)

        setAlbumGroupVisibility(track.collectionName != "")

        binding.playerLikeButton.setImageResource(if (currentTrack.isFavorite) R.drawable.medialib_liked_button else R.drawable.medialib_unlike_button)
    }

    private fun initBottomSheet() {
        binding.playlistsRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.playlistsRecyclerView.adapter = playlistsAdapter

        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.playerPlaylistAddButton.setOnClickListener {
            playlistsAdapter.playlists.clear()
            viewModel.getPlaylists()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        playlistsAdapter.onItemClick = { playlist ->
            onPlaylistClickDebounce(playlist)
        }

        onPlaylistClickDebounce =
            debounceDelay<Playlist>(CLICK_DEBOUNCE_DELAY, lifecycleScope, false) { playlist ->
                viewModel.addTrackInPlaylist(currentTrack, playlist)
            }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }

    private fun setAlbumGroupVisibility(visible: Boolean) {
        binding.playerTrackAlbumData.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.Empty -> showNoPlaylists()
            is PlaylistState.Success -> showSuccess(state.playlists)
        }
    }

    private fun showSuccess(playlists: List<Playlist>) {
        binding.playlistsRecyclerView.visibility = View.VISIBLE

        playlistsAdapter.playlists.clear()
        playlistsAdapter.playlists.addAll(playlists)
        playlistsAdapter.notifyDataSetChanged()
    }

    private fun showNoPlaylists() {
        binding.playlistsRecyclerView.visibility = View.GONE
    }

    companion object {
        const val CURRENT_TRACK_ID = "CURRENT_TRACK_ID"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}