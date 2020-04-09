package com.github.pidsamhai.covid19thailand.di

import androidx.lifecycle.SavedStateHandle
import com.github.pidsamhai.covid19thailand.db.CoVid19Database
import com.github.pidsamhai.covid19thailand.db.network.NetWorkDataSource
import com.github.pidsamhai.covid19thailand.db.network.NetWorkDataSourceImpl
import com.github.pidsamhai.covid19thailand.network.api.CoVid19RapidApiServices
import com.github.pidsamhai.covid19thailand.network.api.Covid19ApiServices
import com.github.pidsamhai.covid19thailand.repository.CoVidRepository
import com.github.pidsamhai.covid19thailand.repository.RapidRepository
import com.github.pidsamhai.covid19thailand.repository.RapidRepositoryImpl
import com.github.pidsamhai.covid19thailand.ui.viewmodel.TimeLineViewModel
import com.github.pidsamhai.covid19thailand.ui.viewmodel.ToDayViewModel
import com.github.pidsamhai.covid19thailand.ui.viewmodel.WorldWideModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModule = module {
    single { SavedStateHandle() }

    single<NetWorkDataSource> {
        NetWorkDataSourceImpl(
            get()
        )
    }
    single<RapidRepository> { RapidRepositoryImpl(get(),get(),get()) }

    viewModel { ToDayViewModel(get(),get()) }
    viewModel { TimeLineViewModel(get()) }
    viewModel { WorldWideModel(get()) }

    single { CoVid19Database.getInstance(androidContext()).todayDao() }
    single { CoVid19Database.getInstance(androidContext()).staticDao() }
    single { CoVid19Database.getInstance(androidContext()).countriesDao() }
    single { CoVid19Database.getInstance(androidContext()).timeLineDao() }

    single { Covid19ApiServices.invoke() }
    single { CoVid19RapidApiServices.invoke() }

    single { CoVidRepository(get(),get(),get()) }
}