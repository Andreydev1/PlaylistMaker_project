package com.example.playlistmaker.library.favorites.domain.models

sealed interface PlaylistState {
    object Empty : PlaylistState

    data class Success(
        val playlists: List<Playlist>
    ) : PlaylistState
}