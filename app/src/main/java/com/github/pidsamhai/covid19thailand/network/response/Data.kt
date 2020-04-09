package com.github.pidsamhai.covid19thailand.network.response


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "timeline_data")
data class Data(
    @SerializedName("Confirmed")
    val confirmed: Int?,
    @PrimaryKey
    @SerializedName("Date")
    val date: String,
    @SerializedName("Deaths")
    val deaths: Int?,
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
    val recovered: Int?
)

data class CoVidDataSets(
    val confirmed: ArrayList<*>,
    val death: ArrayList<*>,
    val recovered: ArrayList<*>,
    val date: ArrayList<*>
) : Serializable {
    fun toList(): List<ArrayList<*>> {
        return listOf(confirmed, death, recovered)
    }
}

typealias confirmed = ArrayList<Entry>
typealias death = ArrayList<Entry>
typealias recovered = ArrayList<Entry>
typealias date = ArrayList<String>

fun List<Data>.toLineDataSet(): CoVidDataSets {
    val confirmed: ArrayList<BarEntry> = ArrayList()
    val death: ArrayList<BarEntry> = ArrayList()
    val recovered: ArrayList<BarEntry> = ArrayList()
    val date: ArrayList<String> = ArrayList()
    this.forEachIndexed { index, data ->
        val indies = index.toFloat()
        confirmed.add(BarEntry(indies, data.confirmed!!.toFloat()))
        death.add(BarEntry(indies, data.deaths!!.toFloat()))
        recovered.add(BarEntry(indies, data.recovered!!.toFloat()))
        date.add(data.date.substring(0, data.date.length - 2))
    }
//    Log.e("toDataset: ", "${confirmed.size}")
    return CoVidDataSets(confirmed, death, recovered, date)
}

fun List<Data>.toBarDataSet(): CoVidDataSets {
    val confirmed: confirmed = ArrayList()
    val death: death = ArrayList()
    val recovered: recovered = ArrayList()
    val date: date = ArrayList()
    this.forEachIndexed { index, data ->
        if (index > 80) {
            confirmed.add(Entry(index.toFloat(), data.confirmed!!.toFloat()))
            death.add(Entry(index.toFloat(), data.deaths!!.toFloat()))
            recovered.add(Entry(index.toFloat(), data.recovered!!.toFloat()))
            date.add(data.date)
        }
    }
//    Log.e("toDataset: ", "${confirmed.size}")
    return CoVidDataSets(confirmed, death, recovered, date)
}