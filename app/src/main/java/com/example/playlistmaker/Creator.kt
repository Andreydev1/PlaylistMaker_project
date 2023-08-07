package com.example.playlistmaker

import com.example.playlistmaker.data.PlayerManagerImpl
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.PlayerPresentation
import com.example.playlistmaker.ui.PlayerActivity

object Creator {
    fun providePresenter(view: PlayerActivity, currentTrack: Track): PlayerPresentation {
        return PlayerPresentation(
            view = view,
            currentTrack = currentTrack,
            playerManager = PlayerManagerImpl()
        )
    }
}