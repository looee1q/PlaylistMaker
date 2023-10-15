package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.creator.Creator

class App: Application() {

    private val getLastSavedThemeUseCase by lazy { Creator.provideGetLastSavedThemeUseCase() }
    private val setNewThemeUseCase by lazy { Creator.provideSetNewThemeUseCase() }

    override fun onCreate() {
        super.onCreate()
        Creator.registryApplication(this)
        setNewThemeUseCase.execute(getLastSavedThemeUseCase.execute())
     }

}