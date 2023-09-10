package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.models.ThemeSettings
import com.example.playlistmaker.domain.models.Track

interface LocalStorage {
    fun removeTrackHistory()
    fun getTrackHistory(): Array<Track>
    fun updateTrackHistory(tracksList: MutableList<Track>)
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}