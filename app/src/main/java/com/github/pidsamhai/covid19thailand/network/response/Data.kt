package com.github.pidsamhai.covid19thailand.network.response


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("Confirmed")
    val confirmed: Int?,
    @SerializedName("Date")
    val date: String?,
    @SerializedName("Deaths")
    val deaths: Int?,
    @SerializedName("Hospitalized")
    val hospitalized: Int?,
    @SerializedName("NewConfirmed")
    val newConfirmed: Int?,
    @SerializedName("NewDeaths")
    val newDeaths: Int?,
    @SerializedName("NewHospitalized")
    val newHospitalized: Int?,
    @SerializedName("NewRecovered")
    val newRecovered: Int?,
    @SerializedName("Recovered")
    val recovered: Int?
)