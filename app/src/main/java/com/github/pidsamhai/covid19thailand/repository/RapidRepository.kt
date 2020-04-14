package com.github.pidsamhai.covid19thailand.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Datas
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.history.History

interface RapidRepository {
    fun fetchStaticsData()
    suspend fun getStatic(country: String) : LiveData<out Datas>
    suspend fun getHistory(country: String) : LiveData<out List<History>>
    suspend fun fetchHistories(country: String)
    val countries: MutableLiveData<List<String>>
    val histories: MutableLiveData<List<History>>
    val static: MutableLiveData<Datas>
}