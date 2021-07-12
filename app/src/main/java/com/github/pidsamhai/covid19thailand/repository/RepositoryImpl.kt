package com.github.pidsamhai.covid19thailand.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.github.pidsamhai.covid19thailand.Result
import com.github.pidsamhai.covid19thailand.db.LastFetch
import com.github.pidsamhai.covid19thailand.db.dao.RapidDao
import com.github.pidsamhai.covid19thailand.db.dao.TimeLineDao
import com.github.pidsamhai.covid19thailand.db.dao.TodayDao
import com.github.pidsamhai.covid19thailand.network.ApiResponse
import com.github.pidsamhai.covid19thailand.network.api.CoVid19RapidApiServices
import com.github.pidsamhai.covid19thailand.network.api.Covid19ApiServices
import com.github.pidsamhai.covid19thailand.network.response.ddc.Data
import com.github.pidsamhai.covid19thailand.network.response.ddc.TimeLine
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import com.github.pidsamhai.covid19thailand.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber

class RepositoryImpl(
    private val covid19ApiServices: Covid19ApiServices,
    private val rapidApiServices: CoVid19RapidApiServices,
    private val todayDao: TodayDao,
    private val rapidDao: RapidDao,
    private val timeLineDao: TimeLineDao,
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
        loadFromDb = { withContext(Dispatchers.IO) { rapidDao.getCountries() } },
        saveCallResult = {
            withContext(Dispatchers.IO) {
                rapidDao.upSertStatics(it.getStaticDatas())
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
        loadFromDb = { withContext(Dispatchers.IO) { rapidDao.getStaticX(country) } },
        saveCallResult = {
            withContext(Dispatchers.IO) {
                rapidDao.upSert(it.apply {
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
        loadFromDb = { withContext(Dispatchers.IO) { todayDao.hashData() } },
        saveCallResult = { withContext(Dispatchers.IO) { todayDao.upSert(it) } },
        shouldFetch = { lastFetch.shouldFetchToday },
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
        loadFromDb = { withContext(Dispatchers.IO) { timeLineDao.getTimeLine() } },
        saveCallResult = { withContext(Dispatchers.IO) { timeLineDao.upSert(it) } },
        shouldFetch = { lastFetch.shouldFetchTimeLine },
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
        loadFromDb = { withContext(Dispatchers.IO) { timeLineDao.getDatas() } },
        saveCallResult = { withContext(Dispatchers.IO) { timeLineDao.upSertDatas(it) } },
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
}