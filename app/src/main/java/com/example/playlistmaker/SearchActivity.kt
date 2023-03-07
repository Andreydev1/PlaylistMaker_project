package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

    private var inputText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val inputSearch = findViewById<EditText>(R.id.et_search)
        val clearImage = findViewById<ImageView>(R.id.iv_search_clear)
        val backLayout = findViewById<androidx.appcompat.widget.Toolbar>(R.id.search_back)

        inputSearch.setText(inputText)

        backLayout.setOnClickListener {
            finish()
        }

        clearImage.setOnClickListener {
            inputSearch.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputSearch.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                inputText = s.toString()
                clearImage.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputSearch.addTextChangedListener(simpleTextWatcher)

        val trackAdapter = TrackAdapter(
            listOf(
                Track(
                    "Smells Like Teen Spirit",
                    "Nirvana",
                    "5:01",
                    getString(R.string.Nirvana_album_cover_img),
                ),
                Track(
                    " Billie Jean",
                    "Michael Jackson",
                    "5:01",
                    getString(R.string.MichaelJackson_album_cover_img),
                ),
                Track(
                    "Stayin' Alive",
                    "Bee Gees",
                    "4:10",
                    getString(R.string.BeeGees_album_cover_img),
                ),
                Track(
                    "Whole Lotta Love",
                    "Led Zeppelin",
                    "5:33",
                    getString(R.string.LedZeppelin_album_cover_img),
                ),
                Track(
                    "Sweet Child O'Mine",
                    "Guns N' Roses",
                    "5:03",
                    getString(R.string.GunsNroses_album_cover_img),
                ),
            ))
        val recyclerView = findViewById<RecyclerView>(R.id.rvTracks)
        recyclerView.adapter = trackAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, inputText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputText = savedInstanceState.getString(SEARCH_TEXT, "")
    }

    private fun clearButtonVisibility(s: CharSequence?) = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
}
