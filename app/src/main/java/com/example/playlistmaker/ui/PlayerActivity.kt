package com.example.playlistmaker.ui


import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.PlayerPresentation
import com.example.playlistmaker.presentation.PlayerView
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity(),PlayerView {

    private lateinit var presenter: PlayerPresentation
    private lateinit var toolBar: androidx.appcompat.widget.Toolbar
    private lateinit var albumCover: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackDuration: TextView
    private lateinit var trackAlbum: TextView
    private lateinit var trackYear: TextView
    private lateinit var trackGenre: TextView
    private lateinit var trackCountry: TextView
    private lateinit var playButton: ImageView
    private lateinit var trackTimer: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        initializePresenter()
        viewFinder()
        setOnClickListeners()

    }
    private fun setOnClickListeners() {
        toolBar.setOnClickListener { finish() }
        playButton.setOnClickListener {
            presenter.switchPlayPause()

        }
    }

    private fun initializePresenter() {
        presenter = Creator.providePresenter(
            view = this,
            currentTrack = intent.getSerializableExtra("track") as Track
        )
    }

    private fun viewFinder() {
        trackTimer = findViewById(R.id.player_timer)
        toolBar = findViewById(R.id.player_back)
        albumCover = findViewById(R.id.player_album_cover)
        trackName = findViewById(R.id.player_track_name)
        artistName = findViewById(R.id.player_artist_name)
        trackDuration = findViewById(R.id.player_track_duration_data)
        trackAlbum = findViewById(R.id.player_track_album_data)
        trackYear = findViewById(R.id.player_track_year_data)
        trackGenre = findViewById(R.id.player_track_genre_data)
        trackCountry = findViewById(R.id.player_track_country_data)
        playButton = findViewById(R.id.player_play_button)
        presenter.setTrackInfo()

    }

    override fun onPause() {
        super.onPause()
        presenter.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.releasePlayer()
        presenter.onViewDestroyed()
    }

    override fun setTrackName(name: String) {
        trackName.text = name
    }

    override fun setArtistName(name: String) {
        artistName.text = name
    }

    override fun setTrackTime(time: String) {
        trackTimer.text = time
    }

    override fun setAlbumName(name: String) {
        trackAlbum.text = name
    }

    override fun setReleaseDate(date: String) {
        trackYear.text = date
    }

    override fun setGenre(name: String) {
        trackGenre.text = name
    }

    override fun setCountry(name: String) {
        trackCountry.text = name
    }

    override fun setCover(coverPath: String) {
        Glide.with(applicationContext)
            .load(coverPath)
            .centerCrop()
            .transform(RoundedCorners(8))
            .placeholder(R.drawable.ic_no_internet)
            .into(albumCover)
    }
    override fun setPlayerStateStart() {
        playButton.setImageResource(R.drawable.media_lib_pause_button)
    }

    override fun setPlayerStatePause() {
        playButton.setImageResource(R.drawable.media_lib_play_button)
    }

    override fun setPlayerStatePrepared() {
        trackTimer.text = "00:00"
        playButton.setImageResource(R.drawable.media_lib_play_button)
    }

    override fun setCurrentTime(time: String) {
        trackTimer.text = time
    }

}