package com.example.playlistmaker.library.domain.models

import com.example.playlistmaker.search.domain.Track

sealed interface FavoritesState {
    object Empty : FavoritesState

    data class Success(
        val tracks: List<Track>
    ) : FavoritesState
}