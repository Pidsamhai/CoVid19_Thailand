package com.github.pidsamhai.covid19thailand.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.pidsamhai.covid19thailand.network.response.Today

@Dao
interface TodayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upSert(todayDao: Today):Long?

    @Query("SELECT * FROM today ORDER BY updateDate DESC LIMIT 1")
    fun getToDay() : LiveData<Today>

    @Query("SELECT * FROM today ORDER BY updateDate DESC LIMIT 1")
    fun hashData() : Today?
}