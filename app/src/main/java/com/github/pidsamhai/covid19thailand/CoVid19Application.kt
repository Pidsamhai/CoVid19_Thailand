package com.github.pidsamhai.covid19thailand

import android.app.Application
import com.github.pidsamhai.covid19thailand.di.databaseModule
import com.github.pidsamhai.covid19thailand.di.repositoryModule
import com.github.pidsamhai.covid19thailand.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class CoVid19Application : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CoVid19Application)
            modules(viewModelModule, databaseModule, repositoryModule)
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return "Logger ${super.createStackElementTag(element)} (${element.lineNumber}): ${element.methodName}"
                }
            })
        }
    }
}