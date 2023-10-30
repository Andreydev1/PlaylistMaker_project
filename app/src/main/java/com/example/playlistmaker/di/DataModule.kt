package com.example.playlistmaker.di

import android.content.Context.MODE_PRIVATE
import android.media.MediaPlayer
import com.example.playlistmaker.player.data.PlayerImpl
import com.example.playlistmaker.player.domain.Player
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.network.ItunesSearchApi
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.ui.SearchHistory
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single { MediaPlayer() }

    factory<Player> {
        PlayerImpl(get())
    }

    single<ItunesSearchApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesSearchApi::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences("PLAYLIST_MAKER_PREFERENCES", MODE_PRIVATE)
    }

    factory { Gson() }

    single<SearchHistory> {
        SearchHistory(get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }


}