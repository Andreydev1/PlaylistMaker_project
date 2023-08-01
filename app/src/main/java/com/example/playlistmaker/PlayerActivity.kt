package com.example.playlistmaker


import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIMER_DELAY = 500L
    }

    private lateinit var toolBar: androidx.appcompat.widget.Toolbar
    private lateinit var currentTrack: Track
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

    private var currentState = STATE_DEFAULT
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var playerHandler = Handler(Looper.getMainLooper())
    private var playerRunnable = Runnable {
        updateTime()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        currentTrack = intent.getSerializableExtra("track") as Track
        viewFinder()
        setOnClickListeners()
        preparePlayer()
        updatePlayerState()
    }
    private fun setOnClickListeners() {
        toolBar.setOnClickListener { finish() }
        playButton.setOnClickListener {
            playBackControl()

        }
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

        if (currentTrack != null) {
            Glide.with(applicationContext)
                .load(currentTrack.getCoverArtWork())
                .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_rounded_corners)))
                .placeholder(R.drawable.media_lib_cover_placeholder)
                .into(albumCover)

            trackName.text = currentTrack.trackName
            artistName.text = currentTrack.artistName
            trackDuration.text =
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(currentTrack.trackTime.toInt())
            trackAlbum.text = currentTrack.collectionName
            trackYear.text = currentTrack.releaseDate.substring(0, 4)
            trackGenre.text = currentTrack.primaryGenreName
            trackCountry.text = currentTrack.country
        }
    }
    private fun preparePlayer() {
        mediaPlayer.setDataSource(currentTrack.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            playButton.isEnabled = true
        }
        mediaPlayer.setOnCompletionListener {
            playerHandler.removeCallbacks(playerRunnable)
            playerState = STATE_PREPARED
            trackTimer.text = getString(R.string.timer_update_to_zero)
            playButton.setImageResource(R.drawable.media_lib_play_button)
        }
    }
    private fun updatePlayerState() {
        currentState = if (playerState == STATE_PLAYING) {
            STATE_PLAYING
        } else {
            STATE_PAUSED
        }
        if (currentState == STATE_PLAYING) {
            playButton.setImageResource(R.drawable.media_lib_pause_button)
            playButton.visibility = View.GONE
        } else {
            playButton.setImageResource(R.drawable.media_lib_play_button)
            playButton.visibility = View.VISIBLE
        }
    }
    private fun updateTime() {
        playerHandler.postDelayed(object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    setTime()
                    playerHandler.postDelayed(this, TIMER_DELAY)
                }
            }
        }, TIMER_DELAY)
    }
    private fun setTime() {
        trackTimer.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
    }
    private fun playBackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        playButton.setImageResource(R.drawable.media_lib_pause_button)
        updateTime()
    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        playButton.setImageResource(R.drawable.media_lib_play_button)
        playButton.visibility = View.VISIBLE
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        playerHandler.removeCallbacks(playerRunnable)
    }
}