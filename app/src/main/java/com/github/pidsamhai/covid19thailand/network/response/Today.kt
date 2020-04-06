package com.github.pidsamhai.covid19thailand.network.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "today")
data class Today(
    @SerializedName("Confirmed")
    val confirmed: Int?,
    @SerializedName("Deaths")
    val deaths: Int?,
    @SerializedName("DevBy")
    val devBy: String?,
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
    val recovered: Int?,
    @SerializedName("SeverBy")
    val severBy: String?,
    @SerializedName("Source")
    val source: String?,
    @PrimaryKey
    @SerializedName("UpdateDate")
    val updateDate: String
)