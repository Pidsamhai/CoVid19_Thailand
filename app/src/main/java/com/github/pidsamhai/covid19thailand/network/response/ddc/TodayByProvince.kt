package com.github.pidsamhai.covid19thailand.network.response.ddc

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

@Keep
@Entity(tableName = "today_by_province")
data class TodayByProvince(
    @PrimaryKey
    @ColumnInfo(name = "province")
    @SerializedName("province")
    val province: String,
    @ColumnInfo(name = "newCase")
    @SerializedName("new_case")
    val newCase: Int?,
    @ColumnInfo(name = "newCaseExcludeAbroad")
    @SerializedName("new_case_excludeabroad")
    val newCaseExcludeAbroad: Int?,
    @ColumnInfo(name = "totalCase")
    @SerializedName("total_case")
    val totalCase: Int?,
    @ColumnInfo(name = "totalCaseExcludeAbroad")
    @SerializedName("total_case_excludeabroad")
    val totalCaseExcludeAbroad: Int?,
    @ColumnInfo(name = "txnDate")
    @SerializedName("txn_date")
    val txnDate: String?,
    @ColumnInfo(name = "updateDate")
    @SerializedName("update_date")
    val updateDate: String?
)