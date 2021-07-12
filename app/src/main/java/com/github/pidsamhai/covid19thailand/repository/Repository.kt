package com.github.pidsamhai.covid19thailand.repository

import androidx.lifecycle.LiveData
import com.github.pidsamhai.covid19thailand.Result
import com.github.pidsamhai.covid19thailand.network.ApiResponse
import com.github.pidsamhai.covid19thailand.network.response.ddc.Data
import com.github.pidsamhai.covid19thailand.network.response.ddc.TimeLine
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Statics
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Datas
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getToday(): ApiResponse<Today>
    suspend fun getTimeLine(): ApiResponse<TimeLine>
    fun getTodayLiveData(): Flow<Result<Today>>
    fun getCountryLiveData(): LiveData<Result<List<String>>>
    fun getStatic(country: String): LiveData<Result<Static>>
    fun getTimeLineLiveData(): LiveData<Result<TimeLine>>
    fun getTimeLineDataLiveData(): LiveData<Result<List<Data>>>
}