package com.github.pidsamhai.covid19thailand.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.pidsamhai.covid19thailand.network.response.rapid.country.Countries
import com.github.pidsamhai.covid19thailand.network.response.rapid.fuckyou.Static
import com.github.pidsamhai.covid19thailand.network.response.rapid.hell.Response

interface RapidRepository {
    fun getStatic(country: String)
    suspend fun getStatic2(country: String) : LiveData<out Static>
    suspend fun fetchCountries()
    val countries: MutableLiveData<List<String>>
    val static: MutableLiveData<Response>
}