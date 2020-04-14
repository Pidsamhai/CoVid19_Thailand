package com.github.pidsamhai.covid19thailand.network.response.rapid.covid193


import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Datas
import com.google.gson.annotations.SerializedName

data class Statics(
    @SerializedName("response")
    val datas: List<Datas>?
) {
    fun getStaticDatas(): List<Datas> {
        val data = ArrayList<Datas>()
        datas?.forEach {
            val temp = it
            temp.country?.let { c ->
                temp.responsePk = c
            }
            data.add(temp)
        }
        return data
    }
}