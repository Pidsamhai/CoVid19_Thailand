package com.github.pidsamhai.covid19thailand.db.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.pidsamhai.covid19thailand.network.api.CoVid19RapidApiServices
import com.github.pidsamhai.covid19thailand.network.api.Covid19ApiServices
import com.github.pidsamhai.covid19thailand.network.response.ddc.TimeLine
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Datas
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.history.History
import timber.log.Timber

class NetWorkDataSourceImpl(private val rapidApiServices: CoVid19RapidApiServices,private val covid19ApiServices: Covid19ApiServices) :
    NetWorkDataSource {

    private val _downloadStatic = MutableLiveData<Static>()
    private val _downloadStatics = MutableLiveData<List<Datas>>()
    private val _downloadHistory = MutableLiveData<Pair<List<History>,String>>()
    private val _downloadToday = MutableLiveData<Today>()
    private val _downloadTimeLine = MutableLiveData<TimeLine>()

    override val downloadHistory: LiveData<Pair<List<History>, String>>
        get() = _downloadHistory

    override val downloadStatic: LiveData<Static>
        get() = _downloadStatic

    override val downloadStatics: LiveData<List<Datas>>
        get() = _downloadStatics

    override val downloadToday: LiveData<Today>
        get() = _downloadToday

    override val downloadTimeLine: LiveData<TimeLine>
        get() = _downloadTimeLine

    override suspend fun fetchStatic(country: String) {
        try {
            val fetchData = rapidApiServices.getStatic(country)
            Timber.e("fetchStatic: ")
            _downloadStatic.postValue(fetchData)
        }catch (e:Exception){
            Timber.e("fetchStatic Error: ")
        }
    }

    override suspend fun fetchStatics() {
        try {
            val fetchData = rapidApiServices.getStatics()
            val response = fetchData.getStaticDatas()
            Timber.e(response.toString())
            _downloadStatics.postValue(response)
        }catch (e:Exception){
            Timber.e("fetchStatics Error")
        }
    }

    override suspend fun fetchHistories( country: String) {
        try {
            val fetchData = rapidApiServices.getHistories(country)
            val response = fetchData.getHistory()
            Timber.e(fetchData.toString())
            _downloadHistory.postValue(Pair(response,country))
        }catch (e:Exception){
            Timber.e(e)
        }
    }

    override suspend fun fetchTimeLine() {
        try {
            val fetchData = covid19ApiServices.getTimeline()
            _downloadTimeLine.postValue(fetchData)
        }catch (e:Exception){
            Timber.e("fetchTimeLine Error: ")
        }
    }

    override suspend fun fetchToday() {
        try {
            val fetchData = covid19ApiServices.getToDay()
            _downloadToday.postValue(fetchData)
        }catch (e:Exception){
            Timber.e("fetchToday Error: ")
        }
    }
}