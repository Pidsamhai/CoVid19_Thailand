package com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

@Entity(tableName = "rapid_statics")
data class Datas(
    @SerializedName("cases")
    val cases: Cases?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("day")
    val day: String?,
    @SerializedName("deaths")
    val deaths: Deaths?,
    @SerializedName("tests")
    val tests: Tests?,
    @SerializedName("time")
    val time: String?
) {
    @PrimaryKey(autoGenerate = false)
    var responsePk: String = "statics_pk"
    fun toGsonString() : String {
        return Gson().toJson(this)
    }
}