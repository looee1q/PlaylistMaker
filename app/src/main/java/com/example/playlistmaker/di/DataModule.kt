package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.playlistmaker.data.db.FavoriteTracksDB
import com.example.playlistmaker.data.db.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.data.player.MediaPlayerImpl
import com.example.playlistmaker.data.search.dao.HistoryTrackListDAOImpl
import com.example.playlistmaker.data.search.dao.SharedPrefTrackListStorage
import com.example.playlistmaker.data.search.dao.TrackListStorage
import com.example.playlistmaker.data.search.network.RetrofitMusicApi
import com.example.playlistmaker.data.settings.SharedPrefThemeStorage
import com.example.playlistmaker.data.settings.ThemeRepositoryImpl
import com.example.playlistmaker.data.settings.ThemeStorage
import com.example.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.example.playlistmaker.domain.favorites.FavoriteTracksRepository
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.search.api.MusicApi
import com.example.playlistmaker.domain.search.dao.HistoryTrackListDAO
import com.example.playlistmaker.domain.settings.repository.ThemeRepository
import com.example.playlistmaker.domain.sharing.repository.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    // modules for settings and sharing

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            SharedPrefThemeStorage.NIGHT_MODE_SWITCH,
            Context.MODE_PRIVATE
        )
    }

    single<ThemeStorage> {
        SharedPrefThemeStorage(get(), get())
    }

    single<ThemeRepository> {
        ThemeRepositoryImpl(get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

    // modules for search

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            SharedPrefTrackListStorage.HISTORY_OF_TRACKS,
            Context.MODE_PRIVATE
        )
    }

    single<TrackListStorage> {
        SharedPrefTrackListStorage(get())
    }

    single<HistoryTrackListDAO> {
        HistoryTrackListDAOImpl(get())
    }

    single<MusicApi> {
        RetrofitMusicApi()
    }

    // modules for player

    single<PlayerRepository> {
        MediaPlayerImpl()
    }

    // modules for RoomDB (favoriteTracks)

    single {
        Room.databaseBuilder(androidContext(), FavoriteTracksDB::class.java, "database.db")
            .build()
    }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(get())
    }
}
