package com.github.pidsamhai.covid19thailand.repository

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.github.pidsamhai.covid19thailand.AppExecutors
import com.github.pidsamhai.covid19thailand.db.TimeLineDao
import com.github.pidsamhai.covid19thailand.db.TodayDao
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
            if (response != null) {
                timeLineDao.upSert(response)
            }
        } catch (e: IOException) {
            Log.e("API", "" + e.message)
        }
    }

//    companion object {
//        @Volatile
//        private var instance: CoVidRepository? = null
//        fun getInstance(todayDao: TodayDao,apiServices: Covid19ApiServices) = instance
//            ?: synchronized(this) {
//                instance
//                    ?: CoVidRepository(
//                        todayDao,
//                        apiServices
//                    )
//                        .also { instance = it }
//            }
//    }
}