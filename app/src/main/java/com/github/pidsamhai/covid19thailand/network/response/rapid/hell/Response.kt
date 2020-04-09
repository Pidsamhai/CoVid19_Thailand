package com.github.pidsamhai.covid19thailand.network.response.rapid.hell


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.pidsamhai.covid19thailand.network.response.rapid.fuckyou.Cases
import com.github.pidsamhai.covid19thailand.network.response.rapid.fuckyou.Deaths
import com.github.pidsamhai.covid19thailand.network.response.rapid.fuckyou.Tests
import com.google.gson.annotations.SerializedName

@Entity(tableName = "rapid_statics")
data class Response(
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
}