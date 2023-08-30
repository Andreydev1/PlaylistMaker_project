package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.App
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.player.domain.PlayerManager
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.data.TracksInteractorImpl
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.player.domain.data.PlayerManagerImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.data.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.SharingRepository
import com.example.playlistmaker.sharing.data.impl.SharingRepositoryImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.data.SharingInteractorImpl

object Creator {
    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(context))
    }

    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(App.instance.getLocalStorage())
    }

    private fun getSharingRepository(context: Context): SharingRepository {
        return SharingRepositoryImpl(context)
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }

    fun providePlayerManager(): PlayerManager {
        return PlayerManagerImpl()
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }

    fun provideSharingInteractor(context: Context, navigator: ExternalNavigator): SharingInteractor {
        return SharingInteractorImpl(navigator, getSharingRepository(context), context)
    }

    private fun provideExternalNavigator(context: Context): ExternalNavigatorImpl {
        return ExternalNavigatorImpl(context)
    }
}