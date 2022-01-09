package com.github.pidsamhai.covid19thailand.network.response.ddc


import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.pidsamhai.covid19thailand.utilities.StatusColors
import com.github.pidsamhai.covid19thailand.utilities.StatusTexts
import com.google.gson.annotations.SerializedName

@Keep
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

data class LineDataSet(
    val title: String,
    val color: String,
    val data: List<Int>
)

fun List<Data>.toDataSet(): List<LineDataSet> {
    val dataset = mutableListOf<LineDataSet>()
    StatusTexts.forEachIndexed { index, s ->
            if(index == 0) { dataset.add(LineDataSet(s, StatusColors[index], this.map { it.confirmed ?: 0 })) }
            if (index == 1) { dataset.add(LineDataSet(s, StatusColors[index], this.map { it.recovered ?: 0 })) }
            if(index == 2) { dataset.add(LineDataSet(s, StatusColors[index], this.map { it.deaths ?: 0 })) }
    }
    return dataset
}