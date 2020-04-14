package com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base


import com.google.gson.annotations.SerializedName

data class Cases(
    @SerializedName("active")
    val active: Int?,
    @SerializedName("critical")
    val critical: Int?,
    @SerializedName("new")
    val new: String?,
    @SerializedName("recovered")
    val recovered: Int?,
    @SerializedName("total")
    val total: Int?
)