package com.example.playlistmaker.search.data

import com.example.playlistmaker.library.data.entity.TrackEntity
import com.example.playlistmaker.search.domain.Track

class DataBaseTrackConvertor {
    fun map(track: TrackEntity): Track {
        return Track(
            track.id,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.id,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            System.currentTimeMillis()
        )
    }
}