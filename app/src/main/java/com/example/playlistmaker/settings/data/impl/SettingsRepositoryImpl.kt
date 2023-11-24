package com.example.playlistmaker.settings.data.impl

import com.example.playlistmaker.search.domain.api.LocalStorage
import com.example.playlistmaker.settings.data.api.SettingsRepository
import com.example.playlistmaker.settings.domain.models.ThemeSettings

class SettingsRepositoryImpl(private val localStorage: LocalStorage): SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        return localStorage.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        localStorage.updateThemeSetting(settings)
    }
}