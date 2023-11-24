package com.example.playlistmaker.di

import com.example.playlistmaker.library.favorites.data.favorites.FavoritesRepositoryImpl
import com.example.playlistmaker.library.favorites.domain.db.FavoritesRepository
import com.example.playlistmaker.library.playlists.data.PlaylistRepositoryImpl
import com.example.playlistmaker.library.playlists.domain.db.PlaylistRepository
import com.example.playlistmaker.library.playlists.newPlaylist.data.NewPlaylistRepositoryImpl
import com.example.playlistmaker.library.playlists.newPlaylist.domain.db.NewPlaylistRepository
import com.example.playlistmaker.search.data.DataBaseTrackConvertor
import com.example.playlistmaker.search.data.PlaylistDbConvertor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.impl.TracksRepositoryImpl
import com.example.playlistmaker.settings.data.api.SettingsRepository
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }
    factory { DataBaseTrackConvertor() }

    factory { PlaylistDbConvertor() }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }
    single<NewPlaylistRepository> {
        NewPlaylistRepositoryImpl(get(), get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get())
    }
}