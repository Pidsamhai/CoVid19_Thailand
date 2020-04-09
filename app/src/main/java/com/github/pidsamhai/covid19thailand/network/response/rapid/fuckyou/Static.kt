package com.github.pidsamhai.covid19thailand.network.response.rapid.fuckyou


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.pidsamhai.covid19thailand.network.response.rapid.hell.Response
import com.google.gson.annotations.SerializedName

@Entity(tableName = "rapid_static")
data class Static(
    @SerializedName("errors")
    val errors: List<String>?,
    @SerializedName("get")
    val `get`: String?,
    @SerializedName("parameters")
    val parameters: Parameters?,
    @SerializedName("response")
    val response: List<Response>?,
    @SerializedName("results")
    val results: Int?
){
    @PrimaryKey(autoGenerate = false)
    var pk:String = "static_pk"
}