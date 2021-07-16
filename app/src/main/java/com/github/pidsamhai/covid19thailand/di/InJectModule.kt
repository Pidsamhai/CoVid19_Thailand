package com.github.pidsamhai.covid19thailand.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.github.pidsamhai.covid19thailand.db.CoVid19Database
import com.github.pidsamhai.covid19thailand.db.LastFetch
import com.github.pidsamhai.covid19thailand.db.LastFetchImpl
import com.github.pidsamhai.covid19thailand.network.api.*
import com.github.pidsamhai.covid19thailand.repository.GithubRepository
import com.github.pidsamhai.covid19thailand.repository.GithubRepositoryImpl
import com.github.pidsamhai.covid19thailand.repository.Repository
import com.github.pidsamhai.covid19thailand.repository.RepositoryImpl
import com.github.pidsamhai.covid19thailand.ui.viewmodel.*
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {

    fun getDatabase(application: Application): CoVid19Database {
        return CoVid19Database.getInstance(application)
    }

    fun getDefaultPref(application: Application): SharedPreferences {
        return application.getSharedPreferences("lastFetch", Context.MODE_PRIVATE)
    }

    single { getDatabase(androidApplication()) }
    single { getDefaultPref(get()) }
    single<LastFetch> { LastFetchImpl(get()) }

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
    viewModel { ToDayViewModel(get(), get()) }
    viewModel { TimeLineViewModel(get()) }
    viewModel { WorldWideModel(get(), get()) }
    viewModel { UpdateDialogVM(get(), get()) }
    viewModel { DownloadDialogVM(get(), get(), get()) }
    viewModel { AboutPageVM(get()) }
}