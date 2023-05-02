package com.example.playlistmaker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

    enum class SearchStatus {
        SUCCESS,
        NOTHING_FOUND,
        NO_INTERNET
    }

    private lateinit var errorText: TextView
    private lateinit var errorImage: ImageView
    private lateinit var inputSearch: EditText
    private lateinit var clearImage: ImageView
    private lateinit var backLayout: Toolbar
    private lateinit var searchRv: RecyclerView
    private lateinit var refreshButton: Button
    private lateinit var somethingWentWrong: LinearLayout
    private lateinit var searchHistoryLayout: LinearLayout
    private lateinit var searchHistoryRv: RecyclerView
    private lateinit var searchHistoryClearButton: Button
    private lateinit var history: SearchHistory


    private val trackAdapter = TrackAdapter()
    private val historyAdapter = TrackAdapter()
    private var inputText = ""
    private val itunesBaseUrl = ("https://itunes.apple.com")
    private val itunesRetrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = itunesRetrofit.create(ItunesApi::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        history = SearchHistory(getSharedPreferences(PLAYLIST_MAKER_PREFS, MODE_PRIVATE))

        viewfinder()
        setOnClickListeners()
        recyclerViewModel()
        changeHistoryVisibility()

    }

    private fun viewfinder() {
        inputSearch = findViewById(R.id.et_search)
        clearImage = findViewById(R.id.iv_search_clear)
        backLayout = findViewById(R.id.search_back)
        searchRv = findViewById(R.id.rvTracks)
        refreshButton = findViewById(R.id.search_refresh_button)
        somethingWentWrong = findViewById(R.id.search_something_went_wrong)
        errorImage = findViewById(R.id.search_error_iv)
        errorText = findViewById(R.id.error_text_tv)
        searchHistoryLayout = findViewById(R.id.search_history_layout)
        searchHistoryRv = findViewById(R.id.rvTracksHistory)
        searchHistoryClearButton = findViewById(R.id.search_clear_history_button)

    }

    private fun setOnClickListeners() {
        inputSearch.setText(inputText)
        backLayout.setOnClickListener {
            finish()
        }
        inputSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val inputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(inputSearch.windowToken, 0)
                findTracks()
                true
            } else false
        }
        refreshButton.setOnClickListener {

            findTracks()
        }

        clearImage.setOnClickListener {
            trackAdapter.setTracks(null)
            inputSearch.setText("")
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputSearch.windowToken, 0)

            onSearchResult(SearchStatus.SUCCESS)
            somethingWentWrong.visibility = View.GONE
            changeHistoryVisibility()
        }
        searchHistoryClearButton.setOnClickListener{
            history.clear()
            changeHistoryVisibility()
            historyAdapter.notifyDataSetChanged()

        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                inputText = s.toString()
                clearImage.visibility = clearButtonVisibility(s)
                searchHistoryLayout.visibility = if (inputSearch.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputSearch.setText(inputText)
        inputSearch.addTextChangedListener(simpleTextWatcher)

    }

    private fun changeHistoryVisibility() {
        searchHistoryLayout.visibility = if (history.trackList.size > 0) View.VISIBLE else View.GONE
    }

    private fun recyclerViewModel() {
        trackAdapter.onItemClick = { track ->
            history.add(track)
            historyAdapter.notifyDataSetChanged()
            playerEnable(track)
        }
        historyAdapter.onItemClick = {track -> playerEnable(track) }

        searchRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchRv.adapter = trackAdapter

        historyAdapter.tracks = history.trackList
        searchHistoryRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchHistoryRv.adapter = historyAdapter


    }

    private fun findTracks() {
        itunesService.search(inputSearch.text.toString()).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.results?.isNotEmpty() == true) {
                        trackAdapter.setTracks(response.body()?.results!!)
                        onSearchResult(SearchStatus.SUCCESS)

                    } else
                        trackAdapter.setTracks(null)
                    onSearchResult(SearchStatus.NOTHING_FOUND)
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                onSearchResult(SearchStatus.NO_INTERNET)
            }
        })
    }

    private fun onSearchResult(resultStatus: SearchStatus) {
        when (resultStatus) {
            SearchStatus.SUCCESS -> {

            }
            SearchStatus.NOTHING_FOUND -> {
                somethingWentWrong.visibility = View.VISIBLE
                errorImage.setImageResource(R.drawable.ic_nothing_found)
                errorText.text = getString(R.string.nothing_found_string)
                refreshButton.visibility = View.GONE
            }
            SearchStatus.NO_INTERNET -> {
                trackAdapter.setTracks(null)
                somethingWentWrong.visibility = View.VISIBLE
                errorImage.setImageResource(R.drawable.ic_no_internet)
                errorText.text = getString(R.string.no_internet_search_failure)
                refreshButton.visibility = View.VISIBLE
            }
        }

    }
    private fun playerEnable(track: Track){
        val player = Intent(this, PlayerActivity::class.java)
        player.putExtra("track", track)
        startActivity(player)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, inputText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputText = savedInstanceState.getString(SEARCH_TEXT, "")
        changeHistoryVisibility()
    }

    private fun clearButtonVisibility(s: CharSequence?) =
        if (s.isNullOrEmpty()) View.GONE else View.VISIBLE

}