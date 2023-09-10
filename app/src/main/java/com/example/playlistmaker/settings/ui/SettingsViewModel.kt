package com.example.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.models.Theme
import com.example.playlistmaker.domain.models.ThemeSettings
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractor


class SettingsViewModel(
                        private val sharingInteractor: SharingInteractor,
                        private val settingsInteractor: SettingsInteractor) : ViewModel() {

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    private fun getThemeSettings(): ThemeSettings {
        return settingsInteractor.getThemeSettings()
    }

    fun updateThemeSetting(settings: ThemeSettings) {
        settingsInteractor.updateThemeSetting(settings)
    }

    fun isNightModeChecked(): Boolean {
        return getThemeSettings().theme == Theme.DARK
    }
}
