package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toolbar

class LibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)
        val mediaLibBackbtn = findViewById<androidx.appcompat.widget.Toolbar>(R.id.media_lib_back)
        mediaLibBackbtn.setOnClickListener{
            finish()
        }
    }
}