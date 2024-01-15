package com.example.playlistmaker.library.playlists.createdPlaylist.domain.models

import android.content.Intent
import com.example.playlistmaker.library.favorites.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track

sealed interface CreatedPlaylistState {
    object Empty : CreatedPlaylistState

    object Deleted : CreatedPlaylistState

    data class Success(
        val playlist: Playlist,
        val tracks: List<Track>
    ) : CreatedPlaylistState

    data class Share(
        val intent: Intent
    ) : CreatedPlaylistState
}