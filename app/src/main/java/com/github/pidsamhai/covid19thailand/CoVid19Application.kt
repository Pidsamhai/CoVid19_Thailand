package com.github.pidsamhai.covid19thailand

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.github.pidsamhai.covid19thailand.di.appModule
import com.github.pidsamhai.covid19thailand.di.databaseModule
import com.github.pidsamhai.covid19thailand.di.repositoryModule
import com.github.pidsamhai.covid19thailand.di.viewModelModule
import com.github.pidsamhai.covid19thailand.worker.WidgetUpdateWorker
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CoVid19Application : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CoVid19Application)
            modules(
                appModule,
                viewModelModule,
                databaseModule,
                repositoryModule
            )
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return "Logger ${super.createStackElementTag(element)} (${element.lineNumber}): ${element.methodName}"
                }
            })
        }

        val task = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(1, TimeUnit.HOURS)
            .build()

        with(WorkManager.getInstance(applicationContext)) {
            enqueueUniquePeriodicWork(WidgetUpdateWorker.NAME, ExistingPeriodicWorkPolicy.KEEP, task)
        }
    }
}