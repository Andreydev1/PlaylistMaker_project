package com.example.playlistmaker.domain

import com.example.playlistmaker.data.TrackDto

interface TrackRepository {
    fun findTracks(searchText: String, callback: TrackSearchingCallBack)
}
interface TrackSearchingCallBack{
    fun onSuccess(tracks:ArrayList<TrackDto>)
    fun onFailure()
}