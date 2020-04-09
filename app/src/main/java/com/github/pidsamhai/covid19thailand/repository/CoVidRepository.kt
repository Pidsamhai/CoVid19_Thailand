package com.github.pidsamhai.covid19thailand.repository

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.github.pidsamhai.covid19thailand.AppExecutors
import com.github.pidsamhai.covid19thailand.db.dao.TimeLineDao
import com.github.pidsamhai.covid19thailand.db.dao.TodayDao
import com.github.pidsamhai.covid19thailand.network.api.Covid19ApiServices
import com.github.pidsamhai.covid19thailand.network.response.TimeLine
import com.github.pidsamhai.covid19thailand.network.response.Today
import java.io.IOException

/**
 * Cache Example
 * https://github.com/BakhtarSobat/GitHubList/blob/master/GitHubList/app/src/main/java/com/bsobat/github/repo/GitHubRepository.java
 **/

class CoVidRepository(private val todayDao: TodayDao, private val timeLineDao: TimeLineDao, private val apiServices: Covid19ApiServices) {
    private val executor = AppExecutors()

    val timeLineDatas = timeLineDao.getDatas()

    fun getToDay(): LiveData<Today> {
        executor.networkIO().execute {
            refresh()
        }
        val source: LiveData<Today> = todayDao.getToDay()
        val mediatorLiveData: MediatorLiveData<Today> = MediatorLiveData<Today>()
        mediatorLiveData.addSource(source){
            mediatorLiveData.value = source.value
        }
        return mediatorLiveData
    }

    fun getTimeLine() : LiveData<TimeLine> {
        executor.networkIO().execute {
            refreshTimeline()
        }
        val source: LiveData<TimeLine> = timeLineDao.getTimeLine()
        val mediatorLiveData: MediatorLiveData<TimeLine> = MediatorLiveData<TimeLine>()
        mediatorLiveData.addSource(source){
            mediatorLiveData.value = source.value
        }
        return mediatorLiveData
    }

    fun refreshToday() {
        executor.networkIO().execute {
            refresh()
        }
    }

    @WorkerThread
    private fun refresh() {
        try {
            val response = apiServices.getToDayNomal()?.execute()?.body()
            if (response != null) {
                todayDao.upSert(response)
            }
        } catch (e: IOException) {
            Log.e("API", "" + e.message)
        }
    }

    @WorkerThread
    private fun refreshTimeline() {
        try {
            val response = apiServices.getTimeline()?.execute()?.body()
            if (response?.data != null) {
                timeLineDao.apply {
                    upSert(response)
                    upSertDatas(response.data)
                }
            }
        } catch (e: IOException) {
            Log.e("API", "" + e.message)
        }
    }
}