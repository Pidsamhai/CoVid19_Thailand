package com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.history

import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @SerializedName("response")
    val histories: List<History>?
){
    fun getHistory(): List<History> {
        val data = ArrayList<History>()
        histories?.forEach {
            val temp = it
            temp.historyPk = temp.country + temp.time
            data.add(temp)
        }
        return data
    }
}