package com.github.pidsamhai.covid19thailand

import android.app.Application
import com.github.pidsamhai.covid19thailand.di.myModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CoVid19Application : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CoVid19Application)
            modules(myModule)
        }
    }
}