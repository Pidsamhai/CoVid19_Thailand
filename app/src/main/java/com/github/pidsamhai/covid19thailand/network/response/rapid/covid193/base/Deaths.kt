package com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base


import com.google.gson.annotations.SerializedName

data class Deaths(
    @SerializedName("new")
    val new: String?,
    @SerializedName("total")
    val total: Int?
)