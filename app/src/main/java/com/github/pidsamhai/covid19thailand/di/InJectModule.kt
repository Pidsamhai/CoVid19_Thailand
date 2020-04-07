package com.github.pidsamhai.covid19thailand.di

import com.github.pidsamhai.covid19thailand.db.CovidDatabase
import com.github.pidsamhai.covid19thailand.db.TodayDao
import com.github.pidsamhai.covid19thailand.db.TodayDao_Impl
import com.github.pidsamhai.covid19thailand.network.api.Covid19ApiServices
import com.github.pidsamhai.covid19thailand.repository.CoVidRepository
import com.github.pidsamhai.covid19thailand.ui.viewmodel.TimeLineViewModel
import com.github.pidsamhai.covid19thailand.ui.viewmodel.ToDayViewModel
import com.github.pidsamhai.covid19thailand.ui.viewmodel.ToDayViewModelFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModule = module {
    viewModel { ToDayViewModel(get()) }
    viewModel { TimeLineViewModel(get()) }
    single { CovidDatabase.getInstance(androidContext()).todayDao() }
    single { CovidDatabase.getInstance(androidContext()).timeLineDao() }
    single { Covid19ApiServices.invoke() }
    single { CoVidRepository(get(),get(),get()) }
}