package com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base


import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

@Keep
@Entity(tableName = "rapid_statics")
data class Datas(
    @SerializedName("cases")
    val cases: Cases?,
    @SerializedName("country")
    val country: String = "unknown",
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
    var responsePk: String = "country"
    fun toGsonString() : String {
        return Gson().toJson(this)
    }
}