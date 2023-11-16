package com.example.playlistmaker.library.playlists.newPlaylist.domain.db

import com.example.playlistmaker.library.favorites.domain.models.Playlist

interface NewPlaylistRepository {
    suspend fun savePlaylist(playlist: Playlist)
}