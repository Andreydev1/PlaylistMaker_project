package com.example.playlistmaker.search.data


import com.example.playlistmaker.Resource
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.search.domain.api.TracksRepository


class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                Resource.Success((response as TracksSearchResponse).results.map {
                    convertTrackDto(it)
                })
            }
            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }

    private fun convertTrackDto(trackDto: TrackDto) = Track(
        trackName = trackDto.trackName,
        artistName = trackDto.artistName,
        trackTime = trackDto.trackTime,
        artworkUrl100 = trackDto.artworkUrl100,
        collectionName = trackDto.collectionName,
        releaseDate = trackDto.releaseDate,
        primaryGenreName = trackDto.primaryGenreName,
        country = trackDto.country,
        previewUrl = trackDto.previewUrl
    )

}