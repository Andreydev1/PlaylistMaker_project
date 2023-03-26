package com.example.playlistmaker

import android.content.Context
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
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }
    enum class SearchStatus{
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
    private lateinit var somethingWentWrong:LinearLayout

    private var tracks = ArrayList<Track>()
    private val trackAdapter = TrackAdapter()
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
        viewfinder()
        setOnClickListeners()
        recyclerViewModel()


    }
    private fun viewfinder(){
        inputSearch = findViewById(R.id.et_search)
        clearImage = findViewById(R.id.iv_search_clear)
        backLayout = findViewById(R.id.search_back)
        searchRv = findViewById(R.id.rvTracks)
        refreshButton = findViewById(R.id.search_refresh_button)
        somethingWentWrong = findViewById(R.id.search_something_went_wrong)
        errorImage = findViewById(R.id.search_error_iv)
        errorText = findViewById(R.id.error_text_tv)
    }

    private fun setOnClickListeners(){
        inputSearch.setText(inputText)
        backLayout.setOnClickListener {
            finish()
        }
        inputSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(inputSearch.windowToken, 0)
                findTracks()
                true
            }
            else false
        }
        refreshButton.setOnClickListener{

            findTracks()
        }

        clearImage.setOnClickListener {
            tracks.clear()
            inputSearch.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputSearch.windowToken, 0)

            onSearchResult(SearchStatus.SUCCESS)
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
        inputSearch.setText(inputText)
        inputSearch.addTextChangedListener(simpleTextWatcher)

    }

    private fun recyclerViewModel(){
        trackAdapter.tracks = tracks
        searchRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchRv.adapter = trackAdapter
    }

    private fun findTracks(){
        itunesService.search(inputSearch.text.toString()).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.results?.isNotEmpty() == true) {
                        tracks.clear()
                        tracks.addAll(response.body()?.results!!)
                        onSearchResult(SearchStatus.SUCCESS)
                        trackAdapter.notifyDataSetChanged()
                    } else
                        tracks.clear()
                        onSearchResult(SearchStatus.NOTHING_FOUND)
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                onSearchResult(SearchStatus.NO_INTERNET)
            }
        })
    }
private fun onSearchResult(resultStatus: SearchStatus){
    when(resultStatus){
        SearchStatus.SUCCESS->{
            somethingWentWrong.visibility = View.GONE
        }
        SearchStatus.NOTHING_FOUND->{
            somethingWentWrong.visibility = View.VISIBLE
            errorImage.setImageResource(R.drawable.ic_nothing_found)
            errorText.text = getString(R.string.nothing_found_string)
            refreshButton.visibility = View.GONE
        }
        SearchStatus.NO_INTERNET->{
            tracks.clear()
            somethingWentWrong.visibility = View.VISIBLE
            errorImage.setImageResource(R.drawable.ic_no_internet)
            errorText.text = getString(R.string.no_internet_search_failure)
            refreshButton.visibility = View.VISIBLE
        }
    }

}
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, inputText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputText = savedInstanceState.getString(SEARCH_TEXT, "")
    }
    private fun clearButtonVisibility(s: CharSequence?) =
        if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
}
