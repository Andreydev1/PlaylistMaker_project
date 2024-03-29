package com.example.playlistmaker.di

import com.example.playlistmaker.library.favorites.domain.db.FavoritesInteractor
import com.example.playlistmaker.library.favorites.domain.impl.FavoritesInteractorImpl
import com.example.playlistmaker.library.playlists.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.playlists.domain.impl.PlaylistInteractorImpl
import com.example.playlistmaker.library.playlists.newPlaylist.domain.db.NewPlaylistInteractor
import com.example.playlistmaker.library.playlists.newPlaylist.domain.impl.NewPlaylistInteractorImpl
import com.example.playlistmaker.search.data.localstorage.LocalStorageImpl
import com.example.playlistmaker.search.domain.api.LocalStorage
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.settings.domain.api.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.impl.SettingsInteractor
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<LocalStorage> {
        LocalStorageImpl(get(), get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }
    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }
    single<NewPlaylistInteractor> {
        NewPlaylistInteractorImpl(get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get(), get())
    }
}