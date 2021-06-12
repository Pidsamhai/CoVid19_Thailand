package com.github.pidsamhai.covid19thailand.network.response.ddc

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
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
    @PrimaryKey
    @SerializedName("UpdateDate")
    val updateDate: String
){
    fun toGsonString() : String {
        return Gson().toJson(this)
    }
}