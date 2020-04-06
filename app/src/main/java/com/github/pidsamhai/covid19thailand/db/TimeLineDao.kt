package com.github.pidsamhai.covid19thailand.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.pidsamhai.covid19thailand.network.response.Today

@Dao
interface TimeLineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upSert(todayDao: Today)

    @Query("SELECT * FROM timeline LIMIT 1")
    fun getTimeLine() : LiveData<Today>
}