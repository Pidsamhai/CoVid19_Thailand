package com.github.pidsamhai.covid19thailand.network.response.rapid.hell


import com.google.gson.annotations.SerializedName

data class Statics(
//    @SerializedName("errors")
//    val errors: List<Any>?,
//    @SerializedName("get")
//    val `get`: String?,
//    @SerializedName("parameters")
//    val parameters: List<Any>?,
//    @SerializedName("response")
    val response: List<Response>?,
    @SerializedName("results")
    val results: Int?
) {
    fun getStatics(): List<Response> {
        val data = ArrayList<Response>()
        response?.forEach {
            val temp = it
            temp.country?.let { c ->
                temp.responsePk = c
            }
            data.add(temp)
        }
        return data
    }
}