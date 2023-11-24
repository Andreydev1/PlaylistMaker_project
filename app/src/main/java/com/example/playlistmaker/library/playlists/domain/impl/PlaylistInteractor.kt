package com.example.playlistmaker.library.playlists.domain.impl

import com.example.playlistmaker.library.favorites.domain.models.Playlist
import com.example.playlistmaker.library.playlists.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.playlists.domain.db.PlaylistRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistsRepository: PlaylistRepository) :
    PlaylistInteractor {
    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    override suspend fun addTrackInPlaylist(track: Track, playlist: Playlist): Boolean {
        return playlistsRepository.addTrackInPlaylist(track, playlist)
    }

    override suspend fun getPlaylistsIdsContainTrack(track: Track): List<Long> {
        return playlistsRepository.getPlaylistsIdsContainTrack(track)
    }
}