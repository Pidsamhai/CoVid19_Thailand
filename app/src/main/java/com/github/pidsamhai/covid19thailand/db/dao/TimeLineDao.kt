package com.github.pidsamhai.covid19thailand.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.pidsamhai.covid19thailand.network.response.Data
import com.github.pidsamhai.covid19thailand.network.response.TimeLine
import com.github.pidsamhai.covid19thailand.network.response.Today

@Dao
interface TimeLineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upSert(timeLine: TimeLine)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upSertDatas(data: List<Data>)

    @Query("SELECT * FROM timeline LIMIT 1")
    fun getTimeLine() : LiveData<TimeLine>

    @Query("SELECT * FROM timeline_data ORDER BY date DESC LIMIT 50")
//    @Query("SELECT * FROM timeline_data ")
    fun getDatas() : LiveData<List<Data>>
}