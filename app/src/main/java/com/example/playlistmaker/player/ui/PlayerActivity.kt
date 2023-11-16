package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.view_model.PlayerViewModel
import com.example.playlistmaker.search.domain.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private val viewModel by viewModel<PlayerViewModel>()

    private lateinit var currentTrack: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentTrack = intent.getSerializableExtra("track") as Track
        viewModel.preparePlayer(currentTrack.previewUrl)

        viewModel.observeFavoriteState().observe(this) {
            binding.playerLikeButton.setImageResource(if (it) R.drawable.medialib_liked_button else R.drawable.medialib_unlike_button)
        }

        setTrackInfo(currentTrack)
        setListeners()

        viewModel.observePlayerState().observe(this) {
            binding.playerPlayButton.isEnabled = it.isPlayButtonEnabled
            binding.playerTimer.text = it.progress

            when (it) {
                is PlayerState.Playing -> binding.playerPlayButton.setImageResource(R.drawable.medialib_pause_button)
                else -> binding.playerPlayButton.setImageResource(R.drawable.medialib_play_button)
            }
        }
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
}