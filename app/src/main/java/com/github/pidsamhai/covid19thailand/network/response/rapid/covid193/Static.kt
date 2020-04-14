package com.github.pidsamhai.covid19thailand.network.response.rapid.covid193


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Datas
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Parameters
import com.google.gson.annotations.SerializedName

@Entity(tableName = "rapid_static")
data class Static(
    val parameters: Parameters?,
    @SerializedName("response")
    val datas: List<Datas>?,
    @SerializedName("results")
    val results: Int?
){
    @PrimaryKey(autoGenerate = false)
    var pk:String = "static_pk"
}