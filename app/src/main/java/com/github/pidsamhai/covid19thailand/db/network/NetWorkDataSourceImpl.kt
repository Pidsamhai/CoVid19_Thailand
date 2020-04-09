package com.github.pidsamhai.covid19thailand.db.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.pidsamhai.covid19thailand.network.api.CoVid19RapidApiServices
import com.github.pidsamhai.covid19thailand.network.response.rapid.country.Countries
import com.github.pidsamhai.covid19thailand.network.response.rapid.fuckyou.Static
import com.github.pidsamhai.covid19thailand.network.response.rapid.hell.Response

class NetWorkDataSourceImpl(private val rapidApiServices: CoVid19RapidApiServices) :
    NetWorkDataSource {

    private val _downloadStatic = MutableLiveData<Static>()
    private val _downloadCountries = MutableLiveData<Countries>()
    private val _downloadStatics = MutableLiveData<List<Response>>()

    override val downloadStatic: LiveData<Static>
        get() = _downloadStatic

    override val downloadCountries: LiveData<Countries>
        get() = _downloadCountries

    override val downloadStatics: LiveData<List<Response>>
        get() = _downloadStatics

    override suspend fun fetchStatic(country: String) {
        try {
            val fetchData = rapidApiServices.getStatic(country)
            Log.e("fetchStatic: ", "Trigger")
            _downloadStatic.postValue(fetchData)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override suspend fun fetchStatics() {
        try {
            val fetchData = rapidApiServices.getStatics()
            val response = fetchData.getStatics()
            Log.e("fetchStatics: ", response.toString())
            _downloadStatics.postValue(response)
        }catch (e:Exception){
            Log.e("fetchStatics Error : ", e.message ?: "")
        }
    }

    override suspend fun fetchCountires() {
        try {
            val fetchData = rapidApiServices.getContiresName()
            _downloadCountries.postValue(fetchData)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}