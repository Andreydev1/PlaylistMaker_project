package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        val mediaLibBackbtn = findViewById<androidx.appcompat.widget.Toolbar>(R.id.player_back)
        mediaLibBackbtn.setOnClickListener{
            finish()
        }
    }
}