package com.example.aviasales

import android.app.Application
import com.example.aviasalesanim.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AviasalesApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AviasalesApplication)
            modules(appModule)
        }
    }
}