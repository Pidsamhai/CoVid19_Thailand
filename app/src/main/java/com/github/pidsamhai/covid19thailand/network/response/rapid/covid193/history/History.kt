package com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.history

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Cases
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Deaths
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Tests
import com.google.gson.annotations.SerializedName

@Entity(tableName = "rapid_history")
data class History(
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
){
    @PrimaryKey(autoGenerate = false)
    var historyPk: String = "history_pk"
}