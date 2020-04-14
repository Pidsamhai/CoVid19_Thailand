package com.github.pidsamhai.covid19thailand.repository

import androidx.lifecycle.MutableLiveData
import com.github.pidsamhai.covid19thailand.network.response.ddc.Data
import com.github.pidsamhai.covid19thailand.network.response.ddc.TimeLine
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today


interface CoVidDDCRepository {
    fun fetchTodayData()
    fun fetchTimeline()
    val today: MutableLiveData<Today>
    val timeLine: MutableLiveData<TimeLine>
    val timeLineData: MutableLiveData<List<Data>>
}