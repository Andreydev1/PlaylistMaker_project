package com.example.playlistmaker.search.view_model

import com.example.playlistmaker.search.domain.Track

sealed interface TracksSearchState {
    object Loading : TracksSearchState
    object NothingFound: TracksSearchState
    object NoInternet: TracksSearchState
    object History: TracksSearchState

    data class Success(
        val tracks: List<Track>
    ) : TracksSearchState
}