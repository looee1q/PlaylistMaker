package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.dao.HistoryTrackListDAOImpl
import com.example.playlistmaker.data.dao.SharedPrefTrackListStorage
import com.example.playlistmaker.data.network.RetrofitMusicApi
import com.example.playlistmaker.domain.api.MusicApi
import com.example.playlistmaker.domain.dao.HistoryTrackListDAO
import com.example.playlistmaker.domain.use_case.GetHistoryTrackListFromStorageUseCase
import com.example.playlistmaker.domain.use_case.GetTracksByApiRequestUseCase
import com.example.playlistmaker.domain.use_case.WriteHistoryTrackListToStorageUseCase

object Creator {

    fun provideGetTracksByApiRequestUseCase() : GetTracksByApiRequestUseCase {
        return GetTracksByApiRequestUseCase(provideMusicApi())
    }

    fun provideGetHistoryTrackListFromStorageUseCase(context: Context) : GetHistoryTrackListFromStorageUseCase {
        return GetHistoryTrackListFromStorageUseCase(provideHistoryTrackListDao(context))
    }

    fun provideWriteHistoryTrackListToStorageUseCase(context: Context) : WriteHistoryTrackListToStorageUseCase {
        return WriteHistoryTrackListToStorageUseCase(provideHistoryTrackListDao(context))
    }

    private fun provideMusicApi(): MusicApi {
        return RetrofitMusicApi()
    }

    private fun provideHistoryTrackListDao(context: Context): HistoryTrackListDAO {
        return HistoryTrackListDAOImpl(SharedPrefTrackListStorage(context))
    }
}