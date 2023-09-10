package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.Resource
import com.example.playlistmaker.domain.models.Track

interface TracksRepository {
   fun searchTracks(expression: String): Resource<List<Track>>
}