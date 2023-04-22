package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    lateinit var playerBackButton: androidx.appcompat.widget.Toolbar
    lateinit var currentTrack: Track
    lateinit var albumCover: ImageView
    lateinit var trackName: TextView
    lateinit var artistName: TextView
    lateinit var trackDuration: TextView
    lateinit var trackAlbum: TextView
    lateinit var trackYear: TextView
    lateinit var trackGenre: TextView
    lateinit var trackCountry: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerBackButton = findViewById(R.id.player_back)
        albumCover = findViewById(R.id.player_album_cover)
        trackName = findViewById(R.id.player_track_name)
        artistName = findViewById(R.id.player_artist_name)
        trackDuration = findViewById(R.id.player_track_duration_data)
        trackAlbum = findViewById(R.id.player_track_album_data)
        trackYear = findViewById(R.id.player_track_year_data)
        trackGenre = findViewById(R.id.player_track_genre_data)
        trackCountry = findViewById(R.id.player_track_country_data)

        currentTrack = intent.getSerializableExtra("track") as Track
        viewFinder()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        playerBackButton.setOnClickListener { finish() }
    }

    private fun viewFinder() {
        if (currentTrack != null) {
            Glide.with(applicationContext)
                .load(currentTrack.getCoverArtWork())
                .transform(RoundedCorners(8))
                .placeholder(R.drawable.media_lib_cover_placeholder)
                .into(albumCover)

            trackName.text = currentTrack.trackName
            artistName.text = currentTrack.artistName
            trackDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(currentTrack.trackTime.toInt())
            trackAlbum.text = currentTrack.collectionName
            trackYear.text = currentTrack.releaseDate.substring(0, 4)
            trackGenre.text = currentTrack.primaryGenreName
            trackCountry.text = currentTrack.country
        }
    }
}
