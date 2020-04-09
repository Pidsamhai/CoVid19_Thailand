package com.github.pidsamhai.covid19thailand.network.response.rapid.country


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

private const val PK = 29

@Entity(tableName = "rapid_countries")
data class Countries(
    @SerializedName("errors")
    val errors: List<String>?,
    @SerializedName("get")
    val `get`: String?,
    @SerializedName("parameters")
    val parameters: List<String>?,
    @SerializedName("response")
    val response: List<String>?,
    @SerializedName("results")
    val results: Int?
){
    @PrimaryKey(autoGenerate = false)
    var pk:Int =  PK
}