package com.github.pidsamhai.covid19thailand.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.db.CoVid19Database
import com.github.pidsamhai.covid19thailand.db.LastFetch
import com.github.pidsamhai.covid19thailand.network.ApiResponse
import com.github.pidsamhai.covid19thailand.network.api.CoVid19RapidApiServices
import com.github.pidsamhai.covid19thailand.network.api.Covid19ApiServices
import com.github.pidsamhai.covid19thailand.network.networkBoundResource
import com.github.pidsamhai.covid19thailand.network.response.ddc.Data
import com.github.pidsamhai.covid19thailand.network.response.ddc.TimeLine
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
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
    override suspend fun getToday(): ApiResponse<Today> {
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

    override fun getCountryLiveData(): LiveData<Result<List<String>>> = networkBoundResource(
        loadFromDb = { withContext(Dispatchers.IO) { database.rapidDao.getCountries() } },
        saveCallResult = {
            withContext(Dispatchers.IO) {
                database.rapidDao.upSertStatics(it.getStaticDatas())
            }
        },
        shouldFetch = { true },
        createCall = {
            try {
                ApiResponse.Success(rapidApiServices.getStatics())
            } catch (e: Exception) {
                Timber.e(e)
                ApiResponse.Error(e)
            }
        }
    ).asLiveData()

    override fun getStatic(country: String): LiveData<Result<Static>> = networkBoundResource(
        loadFromDb = { withContext(Dispatchers.IO) { database.rapidDao.getStaticX(country) } },
        saveCallResult = {
            withContext(Dispatchers.IO) {
                database.rapidDao.upSert(it.apply {
                    it.pk = country
                })
            }
        },
        shouldFetch = { true },
        createCall = {
            try {
                ApiResponse.Success(rapidApiServices.getStatic(country))
            } catch (e: Exception) {
                Timber.e(e)
                ApiResponse.Error(e)
            }
        }
    ).asLiveData()

    override fun getTodayLiveData(): Flow<Result<Today>> = networkBoundResource(
        loadFromDb = { withContext(Dispatchers.IO) { database.todayDao.hashData() } },
        saveCallResult = { withContext(Dispatchers.IO) { database.todayDao.upSert(it) } },
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