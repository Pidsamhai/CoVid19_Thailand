package com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.history


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.mikephil.charting.data.BarEntry
import com.github.pidsamhai.covid19thailand.network.response.ddc.CoVidDataSets
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Cases
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Deaths
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Tests
import com.google.gson.annotations.SerializedName
import timber.log.Timber

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

fun List<History>.toLineDataSet(): CoVidDataSets {
    val confirmed: ArrayList<BarEntry> = ArrayList()
    val death: ArrayList<BarEntry> = ArrayList()
    val recovered: ArrayList<BarEntry> = ArrayList()
    val date: ArrayList<String> = ArrayList()
    this.forEachIndexed { index, data ->
        val indies = index.toFloat()
        if(data.cases!!.total != null)
            confirmed.add(BarEntry(indies, data.cases.total!!.toFloat()))
        else
            confirmed.add(BarEntry(indies, 0f))

        if (data.deaths!!.total != null)
            death.add(BarEntry(indies, data.deaths.total!!.toFloat()))
        else
            death.add(BarEntry(indies, 0F))

        if (data.cases.recovered != null)
            recovered.add(BarEntry(indies, data.cases.recovered.toFloat()))
        else
            recovered.add(BarEntry(indies, 0F))
        date.add(data.day ?: "")
    }
    Timber.e("${confirmed.size}")
    return CoVidDataSets(
        confirmed,
        death,
        recovered,
        date
    )
}