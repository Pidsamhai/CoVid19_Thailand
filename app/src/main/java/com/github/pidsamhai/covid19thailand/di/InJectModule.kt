package com.github.pidsamhai.covid19thailand.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.appwidget.ThaiWidgetConfigureVM
import com.github.pidsamhai.covid19thailand.appwidget.WorldWidgetConfigureVM
import com.github.pidsamhai.covid19thailand.db.CoVid19Database
import com.github.pidsamhai.covid19thailand.db.LastFetch
import com.github.pidsamhai.covid19thailand.db.LastFetchImpl
import com.github.pidsamhai.covid19thailand.network.api.*
import com.github.pidsamhai.covid19thailand.repository.GithubRepository
import com.github.pidsamhai.covid19thailand.repository.GithubRepositoryImpl
import com.github.pidsamhai.covid19thailand.repository.Repository
import com.github.pidsamhai.covid19thailand.repository.RepositoryImpl
import com.github.pidsamhai.covid19thailand.ui.viewmodel.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val databaseModule = module {

    fun getDatabase(application: Application): CoVid19Database {
        return CoVid19Database.getInstance(application)
    }

    fun getDefaultPref(application: Application): SharedPreferences {
        return application.getSharedPreferences("lastFetch", Context.MODE_PRIVATE)
    }

    fun getWidgetPref(application: Application): SharedPreferences {
        return application.getSharedPreferences("widget", Context.MODE_PRIVATE)
    }

    single { getDatabase(androidApplication()) }
    single(named("widgetPref")) { getWidgetPref(androidApplication()) }
    single { getDefaultPref(get()) }
    single<LastFetch> { LastFetchImpl(get()) }
    single(named("defaultPref")) {
        PreferenceManager.getDefaultSharedPreferences(androidApplication())
    }
}

val repositoryModule = module {
    single { Covid19ApiServices.create() }
    single { CoVid19RapidApiServices.create() }
    single { GithubApiService.create() }
    single<DownloadUpdateService> { DownloadUpdateServiceImpl() }
    single<Repository> { RepositoryImpl(get(), get(), get(), get()) }
    single<GithubRepository> { GithubRepositoryImpl(get(), get()) }
}

val viewModelModule = module {
    viewModel { ToDayViewModel(get(), get(), get()) }
    viewModel { TimeLineViewModel(get()) }
    viewModel { WorldWideModel(get(), get()) }
    viewModel { UpdateDialogVM(get(), get(), get(named("defaultPref"))) }
    viewModel { DownloadDialogVM(get(), get(), get()) }
    viewModel { AboutPageVM(get()) }
    viewModel { ThaiWidgetConfigureVM(get(), get(named("widgetPref"))) }
    viewModel { WorldWidgetConfigureVM(get(), get(named("widgetPref"))) }
}

val appModule = module {
    fun provideRemoteConfig(): FirebaseRemoteConfig {
        return Firebase.remoteConfig.apply {
            setConfigSettingsAsync(
                remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 3600
                }
            )
            setDefaultsAsync(R.xml.default_config)
        }
    }

    single { provideRemoteConfig() }
}