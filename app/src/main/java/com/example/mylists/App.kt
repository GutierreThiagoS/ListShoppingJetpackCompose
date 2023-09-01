package com.example.mylists

import android.app.Application
import com.example.mylists.di.getKoinModuleList
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    companion object{
        lateinit var context: Application
    }

    override fun onCreate() {
        super.onCreate()

        context = this

        startKoin {
            androidContext(this@App)
            modules(getKoinModuleList())
        }

    }
}