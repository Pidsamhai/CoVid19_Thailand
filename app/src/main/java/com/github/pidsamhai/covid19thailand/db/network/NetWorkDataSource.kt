package com.github.pidsamhai.covid19thailand.db.network

import androidx.lifecycle.LiveData
import com.github.pidsamhai.covid19thailand.network.response.rapid.country.Countries
import com.github.pidsamhai.covid19thailand.network.response.rapid.fuckyou.Static
import com.github.pidsamhai.covid19thailand.network.response.rapid.hell.Response

interface NetWorkDataSource {
    val downloadCountries: LiveData<Countries>
    val downloadStatic: LiveData<Static>
    val downloadStatics: LiveData<List<Response>>

    suspend fun fetchStatic(
        country:String
    )

    suspend fun fetchStatics()

    suspend fun fetchCountires()


}