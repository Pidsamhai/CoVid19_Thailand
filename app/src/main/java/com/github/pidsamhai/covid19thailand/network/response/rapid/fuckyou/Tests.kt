package com.github.pidsamhai.covid19thailand.network.response.rapid.fuckyou


import com.google.gson.annotations.SerializedName

data class Tests(
    @SerializedName("total")
    val total: Int?
)