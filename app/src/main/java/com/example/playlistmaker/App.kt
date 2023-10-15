package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.domainModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.domain.settings.use_cases.implementations.GetLastSavedThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.use_cases.implementations.SetNewThemeUseCaseImpl
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    val getLastSavedThemeUseCase: GetLastSavedThemeUseCaseImpl by inject()
    val setNewThemeUseCase: SetNewThemeUseCaseImpl by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, domainModule, viewModelModule)
        }

        Creator.registryApplication(this)
//        setNewThemeUseCase.execute(getLastSavedThemeUseCase.execute())
     }

}