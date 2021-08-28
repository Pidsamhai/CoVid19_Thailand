package com.github.pidsamhai.covid19thailand.network.response.rapid.covid193

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CountiesResponse(
    @SerializedName("response")
    private val _countries: List<String>
) {
    val countries: List<Country>
        get() = _countries.map { Country(it) }
}