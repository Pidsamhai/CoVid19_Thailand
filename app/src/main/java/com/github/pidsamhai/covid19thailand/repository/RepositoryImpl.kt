package com.github.pidsamhai.covid19thailand.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.github.pidsamhai.covid19thailand.db.CoVid19Database
import com.github.pidsamhai.covid19thailand.db.LastFetch
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.network.ApiResponse
import com.github.pidsamhai.covid19thailand.network.api.CoVid19RapidApiServices
import com.github.pidsamhai.covid19thailand.network.api.Covid19ApiServices
import com.github.pidsamhai.covid19thailand.network.networkBoundResource
import com.github.pidsamhai.covid19thailand.network.response.ddc.*
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber

class RepositoryImpl(
    private val covid19ApiServices: Covid19ApiServices,
    private val rapidApiServices: CoVid19RapidApiServices,
    private val database: CoVid19Database,
    private val lastFetch: LastFetch
) : Repository {
    override suspend fun getToday(): ApiResponse<TodayResponse> {
        return try {
            val result = covid19ApiServices.getToDay()
            lastFetch.saveLastFetchToday()
            ApiResponse.Success(result)
        } catch (e: Exception) {
            Timber.e(e)
            ApiResponse.Error(e)
        }
    }

    override suspend fun getTimeLine(): ApiResponse<TimeLine> {
        return try {
            val result = covid19ApiServices.getTimeline()
            lastFetch.saveLastFetchTimeline()
            ApiResponse.Success(result)
        } catch (e: Exception) {
            Timber.e(e)
            ApiResponse.Error(e)
        }
    }

    override fun getCountries(): Flow<Result<List<String>>> = networkBoundResource(
        query = { database.rapidDao.getCountries() },
        saveFetchResult = {
            database.rapidDao.upSertCountries(it.countries)
        },
        shouldFetch = { it?.isNullOrEmpty() == true },
        fetch = { rapidApiServices.getCountries() }
    )

    override fun getStatic(country: String): Flow<Result<Static>> = networkBoundResource(
        loadFromDb = { database.rapidDao.getStaticX(country) },
        saveCallResult = {
            database.rapidDao.upSert(it.apply {
                it.pk = country
            })
        },
        createCall = {
            try {
                ApiResponse.Success(rapidApiServices.getStatic(country))
            } catch (e: Exception) {
                Timber.e(e)
                ApiResponse.Error(e)
            }
        }
    )

    override fun getTodayFlow(): Flow<Result<Today>> = networkBoundResource(
        loadFromDb = { withContext(Dispatchers.IO) { database.todayDao.hashData() } },
        saveCallResult = {
            withContext(Dispatchers.IO) {
                it.today?.let { it1 -> database.todayDao.upSert(it1) }
            }
        },
        shouldFetch = { lastFetch.shouldFetchToday || it == null },
        createCall = {
            try {
                ApiResponse.Success(covid19ApiServices.getToDay()).also {
                    lastFetch.saveLastFetchToday()
                }
            } catch (e: Exception) {
                Timber.e(e)
                ApiResponse.Error(e)
            }
        }
    )

    override fun getTodayByProvince(
        province: String,
        forceRefresh: Boolean
    ): Flow<Result<TodayByProvince>> =
        networkBoundResource(
            loadFromDb = {
                withContext(Dispatchers.IO) {
                    database.todayByProvince.getTodayByProvince(
                        province
                    )
                }
            },
            saveCallResult = {
                withContext(Dispatchers.IO) {
                    database.todayByProvince.upSert(it)
                }
            },
            shouldFetch = { (lastFetch.shouldFetchTodayByProvince || it == null) || forceRefresh },
            createCall = {
                try {
                    ApiResponse.Success(covid19ApiServices.getTodayCaseByProvince()).also {
                        lastFetch.saveLastFetchTodayByProvince()
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                    ApiResponse.Error(e)
                }
            }
        )

    override fun getTodayByProvinces(forceRefresh: Boolean): Flow<Result<List<TodayByProvince>>> =
        networkBoundResource(
            loadFromDb = { withContext(Dispatchers.IO) { database.todayByProvince.getTodayByProvinces() } },
            saveCallResult = {
                withContext(Dispatchers.IO) {
                    database.todayByProvince.upSert(it)
                }
            },
            shouldFetch = { (lastFetch.shouldFetchTodayByProvince || it == null) || forceRefresh },
            createCall = {
                try {
                    ApiResponse.Success(covid19ApiServices.getTodayCaseByProvince()).also {
                        lastFetch.saveLastFetchTodayByProvince()
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                    ApiResponse.Error(e)
                }
            }
        )

    override fun getTimeLineLiveData(): LiveData<Result<TimeLine>> = networkBoundResource(
        loadFromDb = { withContext(Dispatchers.IO) { database.timeLineDao.getTimeLine() } },
        saveCallResult = { withContext(Dispatchers.IO) { database.timeLineDao.upSert(it) } },
        shouldFetch = { lastFetch.shouldFetchTimeLine || it == null },
        createCall = {
            try {
                ApiResponse.Success(covid19ApiServices.getTimeline()).also {
                    lastFetch.saveLastFetchTimeline()
                }
            } catch (e: Exception) {
                Timber.e(e)
                ApiResponse.Error(e)
            }
        }
    ).asLiveData()

    override fun getTimeLineDataLiveData(): LiveData<Result<List<Data>>> = networkBoundResource(
        loadFromDb = { withContext(Dispatchers.IO) { database.timeLineDao.getDatas() } },
        saveCallResult = { withContext(Dispatchers.IO) { database.timeLineDao.upSertDatas(it) } },
        shouldFetch = { true },
        createCall = {
            try {
                ApiResponse.Success(covid19ApiServices.getTimeline().data ?: listOf())
            } catch (e: Exception) {
                Timber.e(e)
                ApiResponse.Error(e)
            }
        }
    ).asLiveData()

    override fun clearDataBase() {
        database.clearAllTables()
    }
}