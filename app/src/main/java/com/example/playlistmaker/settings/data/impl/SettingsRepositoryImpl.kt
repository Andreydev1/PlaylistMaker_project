package com.example.playlistmaker.settings.data.impl

import com.example.playlistmaker.domain.LocalStorage
import com.example.playlistmaker.domain.models.ThemeSettings
import com.example.playlistmaker.settings.data.SettingsRepository

class SettingsRepositoryImpl(val localStorage: LocalStorage): SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        return localStorage.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        localStorage.updateThemeSetting(settings)
    }
}