package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.utils.Resource

interface TracksRepository {
   fun searchTracks(expression: String): Resource<List<Track>>
}