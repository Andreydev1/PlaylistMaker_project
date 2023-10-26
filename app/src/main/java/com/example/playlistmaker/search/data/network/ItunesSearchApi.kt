package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesSearchApi {
    @GET("/search?entity=song")
    fun search(@Query("term") term: String): TracksSearchResponse
}