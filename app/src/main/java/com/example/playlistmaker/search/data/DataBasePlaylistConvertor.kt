package com.example.playlistmaker.search.data

import com.example.playlistmaker.library.favorites.data.entity.PlaylistEntity
import com.example.playlistmaker.library.favorites.data.entity.TrackLibraryEntity
import com.example.playlistmaker.library.favorites.data.entity.TrackPlaylistEntity
import com.example.playlistmaker.library.favorites.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track

class PlaylistDbConvertor {
    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.coverPath,
            playlist.size
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.coverPath,
            playlist.size,
            System.currentTimeMillis()
        )
    }

    fun map(track: Track): TrackLibraryEntity {
        return TrackLibraryEntity(
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

    fun map(trackId: Long, playlistId: Long): TrackPlaylistEntity {
        return TrackPlaylistEntity(trackId, playlistId)
    }

}