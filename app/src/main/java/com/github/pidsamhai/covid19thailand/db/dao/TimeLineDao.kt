package com.github.pidsamhai.covid19thailand.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.pidsamhai.covid19thailand.network.response.ddc.Data
import com.github.pidsamhai.covid19thailand.network.response.ddc.TimeLine

@Dao
interface TimeLineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upSert(timeLine: TimeLine)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upSertDatas(data: List<Data>)

    @Query("SELECT * FROM timeline LIMIT 1")
    fun getTimeLine(): TimeLine?

    @Query(
        "SELECT * FROM " +
                "(SELECT * FROM (SELECT" +
                "(substr(`date`, 7,4) || '-' ||" +
                "substr(`date`, 4, 2)|| '-' ||" +
                "substr(`date`, 1, 2)) as date," +
                "confirmed," +
                "hospitalized," +
                "recovered," +
                "deaths," +
                "newConfirmed," +

                "newHospitalized," +
                "newRecovered," +
                "newDeaths " +
                "FROM timeline_data) ORDER by strftime(`date`) DESC LIMIT 5) " +
                "ORDER BY date ASC"
    )
    fun getDatas(): List<Data>
}