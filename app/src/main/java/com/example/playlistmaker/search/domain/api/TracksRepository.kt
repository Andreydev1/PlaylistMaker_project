package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.utils.Resource
import com.example.playlistmaker.search.domain.Track

interface TracksRepository {
   fun searchTracks(expression: String): Resource<List<Track>>
}