package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun favoritesTracks(): Flow<List<Track>>
    suspend fun saveTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    suspend fun getFavoriteTracksIds(): Flow<List<Long>>
}