package com.example.playlistmaker.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TracksSearchState
import com.example.playlistmaker.search.ui.SearchHistory
import com.example.playlistmaker.utils.debounceDelay
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TracksSearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val history: SearchHistory,
    private val tracksRepository: TracksRepository
) : ViewModel() {


    private var lastSearchText: String? = null
    private var searchJob: Job? = null

    private val tracksSearchDebounce =
        debounceDelay<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            findTracks(changedText)
        }

    private val historyState = MutableLiveData<List<Track>>()

    private val stateLiveData = MutableLiveData<TracksSearchState>()
    fun observeState(): LiveData<TracksSearchState> = stateLiveData
    fun observeHistoryState(): LiveData<List<Track>> = historyState
    fun addTrackToHistory(track: Track) {
        history.add(track)
    }

    private fun findTracks(newSearchText: String) {
        setLastSearchText(newSearchText)
        if (newSearchText.isEmpty()) return

        renderState(TracksSearchState.Loading)

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            tracksInteractor
                .searchTracks(newSearchText)
                .collect { pair ->
                    processResult(pair.first, pair.second)
                }
            searchJob = null
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks)
            renderState(TracksSearchState.Success(foundTracks))
        }
        if (errorMessage != null) {
            renderState(TracksSearchState.NoInternet)
        } else if (tracks.isEmpty()) {
            renderState(TracksSearchState.NothingFound)
        } else {
            renderState(TracksSearchState.Success(tracks))
        }
    }

    fun isHistoryEmpty() = history.tracksList.isEmpty()

    fun clearHistory() {
        history.clear()
        renderState(TracksSearchState.History)
    }

    fun clearSearchingText() {
        updateHistoryList()
        renderState(TracksSearchState.History)
    }

    fun searchDebounce(changedText: String) {
        if (lastSearchText != changedText) {
            lastSearchText = changedText
            tracksSearchDebounce(changedText)
        }
    }

    fun startSearch() {
        val newSearchText = lastSearchText
        findTracks(newSearchText ?: "")
    }

    fun setLastSearchText(changedText: String) {
        this.lastSearchText = changedText
    }

    fun getLastSearchText(): String {
        return lastSearchText ?: ""
    }

    fun updateHistoryList() {
        signHistoryList(history.tracksList)
    }

    private fun renderState(state: TracksSearchState) {
        stateLiveData.postValue(state)
    }

    private fun signHistoryList(tracks: List<Track>) {
        viewModelScope.launch {
            tracksRepository.getFavoriteTrackList(tracks)
                .collect { trackList ->
                    historyState.postValue(trackList)
                }
        }
    }

    fun updateSearchResults(tracks: List<Track>) {
        if (tracks.isNotEmpty()) {
            signSearchResult(tracks)
        }
    }

    private fun signSearchResult(tracks: List<Track>) {
        viewModelScope.launch {
            tracksRepository
                .getFavoriteTrackList(tracks)
                .collect { trackList ->
                    renderState(TracksSearchState.Success(trackList))
                }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}

