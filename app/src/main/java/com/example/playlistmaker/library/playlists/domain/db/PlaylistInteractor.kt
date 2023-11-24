package com.example.playlistmaker.library.playlists.domain.db

import com.example.playlistmaker.library.favorites.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addTrackInPlaylist(track: Track, playlist: Playlist): Boolean
    suspend fun getPlaylistsIdsContainTrack(track: Track): List<Long>
}