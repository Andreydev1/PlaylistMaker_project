package com.example.playlistmaker.library.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R

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