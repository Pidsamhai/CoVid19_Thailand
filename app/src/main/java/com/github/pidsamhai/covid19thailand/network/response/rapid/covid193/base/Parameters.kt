package com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Parameters(
    @SerializedName("country")
    val country: String?
)