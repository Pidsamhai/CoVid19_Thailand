package com.github.pidsamhai.covid19thailand.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.pidsamhai.covid19thailand.network.response.ddc.TodayByProvince
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Datas
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.history.History

@Dao
interface TodayByProvinceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upSert(list: List<TodayByProvince>)

    @Query("SELECT * FROM today_by_province WHERE province = :province")
    fun getTodayByProvince(province: String): TodayByProvince?

    @Query("SELECT * FROM today_by_province")
    fun getTodayByProvinces(): List<TodayByProvince>
}