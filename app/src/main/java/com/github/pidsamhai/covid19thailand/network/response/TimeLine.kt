package com.github.pidsamhai.covid19thailand.network.response


import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "timeline")
data class TimeLine(
    @SerializedName("DevBy")
    val devBy: String?,
    @SerializedName("SeverBy")
    val severBy: String?,
    @SerializedName("Source")
    val source: String?,
    @PrimaryKey
    @SerializedName("UpdateDate")
    val updateDate: String
){
    @Ignore
    @SerializedName("Data")
    val data: List<Data>? = null
}