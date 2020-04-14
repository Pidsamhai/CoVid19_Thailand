package com.github.pidsamhai.covid19thailand.db.network

import androidx.lifecycle.LiveData
import com.github.pidsamhai.covid19thailand.network.response.ddc.TimeLine
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Datas
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.history.History

interface NetWorkDataSource {
    val downloadStatic: LiveData<Static>
    val downloadStatics: LiveData<List<Datas>>
    val downloadHistory: LiveData<Pair<List<History>,String>>
    val downloadToday: LiveData<Today>
    val downloadTimeLine: LiveData<TimeLine>

    suspend fun fetchStatic(
        country:String
    )

    suspend fun fetchStatics()

    suspend fun fetchToday()

    suspend fun fetchHistories( country: String)

    suspend fun fetchTimeLine()


}