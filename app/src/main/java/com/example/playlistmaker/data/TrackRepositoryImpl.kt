package com.example.playlistmaker.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.playlistmaker.domain.TrackRepository
import com.example.playlistmaker.domain.TrackSearchingCallBack
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TracksDtoResponse(val results: List<TrackDto>)

interface ItunesApi {
    @GET("/search?entity=song")
    fun search(@Query("term") term: String): Call<TracksDtoResponse>
}

class TrackRepositoryImpl:TrackRepository {
    private val itunesBaseUrl = ("https://itunes.apple.com")
    private val itunesRetrofit =
        Retrofit.Builder().baseUrl(itunesBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()
    private val itunesService = itunesRetrofit.create(ItunesApi::class.java)


    override fun findTracks(searchText: String, callback: TrackSearchingCallBack) {
        itunesService.search(searchText).enqueue(object : Callback<TracksDtoResponse> {
            override fun onResponse(
                call: Call<TracksDtoResponse>,
                response: Response<TracksDtoResponse>)
             {
                if (response.code() ==200) {
                    val tracksList = ArrayList<TrackDto>()
                    val tracksDtoResponse = response.body()?.results!!
                    if (tracksDtoResponse.isNotEmpty()) {
                            tracksList.addAll(tracksDtoResponse)
                        }
                else {
                      callback.onSuccess(tracksList)
                    }
                } else {
                    callback.onFailure()
                }
            }

            override fun onFailure(call: Call<TracksDtoResponse>, t: Throwable) {

                callback.onFailure()
            }
        })
    }
}



