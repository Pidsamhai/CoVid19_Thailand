package com.github.pidsamhai.covid19thailand.repository

import androidx.lifecycle.LiveData
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.network.ApiResponse
import com.github.pidsamhai.covid19thailand.network.response.ddc.*
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Country
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getToday(): ApiResponse<TodayResponse>
    suspend fun getTimeLine(): ApiResponse<TimeLine>
    fun getTodayFlow(): Flow<Result<Today>>
    fun getCountries(): Flow<Result<List<String>>>
    fun getStatic(country: String): Flow<Result<Static>>
    fun getTimeLineLiveData(): LiveData<Result<TimeLine>>
    fun getTimeLineDataLiveData(): LiveData<Result<List<Data>>>
    fun clearDataBase()
    fun getTodayByProvince(province: String, forceRefresh: Boolean = false): Flow<Result<TodayByProvince>>
    fun getTodayByProvinces(forceRefresh: Boolean = false): Flow<Result<List<TodayByProvince>>>
}