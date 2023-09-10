package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.creator.Resource
import com.example.playlistmaker.domain.models.Track
import javax.xml.xpath.XPathExpression

interface TracksRepository {
   fun searchTracks(expression: String): Resource<List<Track>>
}