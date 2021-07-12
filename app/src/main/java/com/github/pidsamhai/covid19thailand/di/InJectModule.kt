package com.github.pidsamhai.covid19thailand.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import com.github.pidsamhai.covid19thailand.BuildConfig
import com.github.pidsamhai.covid19thailand.db.CoVid19Database
import com.github.pidsamhai.covid19thailand.db.LastFetch
import com.github.pidsamhai.covid19thailand.db.LastFetchImpl
import com.github.pidsamhai.covid19thailand.db.dao.RapidDao
import com.github.pidsamhai.covid19thailand.db.dao.TimeLineDao
import com.github.pidsamhai.covid19thailand.db.dao.TodayDao
import com.github.pidsamhai.covid19thailand.network.api.CoVid19RapidApiServices
import com.github.pidsamhai.covid19thailand.network.api.Covid19ApiServices
import com.github.pidsamhai.covid19thailand.repository.*
import com.github.pidsamhai.covid19thailand.ui.viewmodel.TimeLineViewModel
import com.github.pidsamhai.covid19thailand.ui.viewmodel.ToDayViewModel
import com.github.pidsamhai.covid19thailand.ui.viewmodel.WorldWideModel
import com.github.pidsamhai.covid19thailand.utilities.OWNER
import com.github.pidsamhai.covid19thailand.utilities.REPOSITORY
import com.github.pidsamhai.gitrelease.GitRelease
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {

    fun getDatabase(application: Application): CoVid19Database {
        return CoVid19Database.getInstance(application)
    }

    fun getRapidDao(database: CoVid19Database): RapidDao {
        return database.rapidDao()
    }

    fun getTodayDao(database: CoVid19Database): TodayDao {
        return database.todayDao()
    }

    fun getTimelinedayDao(database: CoVid19Database): TimeLineDao {
        return database.timeLineDao()
    }

    fun getDefaultPref(application: Application): SharedPreferences {
        return application.getSharedPreferences("lastFetch", Context.MODE_PRIVATE)
    }

    single { getDatabase(androidApplication()) }
    single { getRapidDao(get()) }
    single { getTodayDao(get()) }
    single { getTimelinedayDao(get()) }
    single { getDefaultPref(get()) }
    single<LastFetch> { LastFetchImpl(get()) }

}

val repositoryModule = module {
    single { Covid19ApiServices.create() }
    single { CoVid19RapidApiServices.create() }
    single<Repository> { RepositoryImpl(get(), get(), get(), get(), get(), get()) }
}

val viewModelModule = module {
    single { SavedStateHandle() }
    viewModel { ToDayViewModel(get(), get()) }
    viewModel { TimeLineViewModel(get()) }
    viewModel { WorldWideModel(get(), get()) }
    single { (activity: Activity) ->
        GitRelease(activity,
                OWNER,
                REPOSITORY,
                BuildConfig.VERSION_NAME
        )
    }
}