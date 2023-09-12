package com.example.playlistmaker.settings.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.models.Theme
import com.example.playlistmaker.settings.domain.models.ThemeSettings
import com.example.playlistmaker.settings.domain.impl.SettingsInteractor
import com.example.playlistmaker.sharing.domain.api.SharingInteractor


class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

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
