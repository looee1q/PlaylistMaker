package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.domainModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.domain.settings.use_cases.interfaces.GetLastSavedThemeUseCase
import com.example.playlistmaker.domain.settings.use_cases.interfaces.SetNewThemeUseCase
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    private val getLastSavedThemeUseCase: GetLastSavedThemeUseCase by inject()
    private val setNewThemeUseCase: SetNewThemeUseCase by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, domainModule, viewModelModule)
        }

        setNewThemeUseCase.execute(getLastSavedThemeUseCase.execute())
     }

}