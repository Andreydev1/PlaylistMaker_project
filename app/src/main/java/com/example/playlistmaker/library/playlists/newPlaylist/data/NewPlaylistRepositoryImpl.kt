package com.example.playlistmaker.library.playlists.newPlaylist.data

import com.example.playlistmaker.library.favorites.data.AppDataBase
import com.example.playlistmaker.library.favorites.domain.models.Playlist
import com.example.playlistmaker.library.playlists.newPlaylist.domain.db.NewPlaylistRepository
import com.example.playlistmaker.search.data.PlaylistDbConvertor

class NewPlaylistRepositoryImpl(
    private val appDatabase: AppDataBase,
    private val playlistDbConvertor: PlaylistDbConvertor
) : NewPlaylistRepository {
    override suspend fun savePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlistDbConvertor.map(playlist))
    }

}