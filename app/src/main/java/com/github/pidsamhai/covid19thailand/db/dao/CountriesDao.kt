package com.github.pidsamhai.covid19thailand.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.pidsamhai.covid19thailand.network.response.rapid.country.Countries

@Dao
interface CountriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upSert(timeLine: Countries)

    @Query("SELECT * FROM rapid_countries LIMIT 1")
    fun getCountires(): LiveData<Countries>

    @Query("SELECT * FROM rapid_countries LIMIT 1")
    fun getCountiresReal(): Countries
}