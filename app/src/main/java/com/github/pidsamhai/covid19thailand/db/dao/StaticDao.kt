package com.github.pidsamhai.covid19thailand.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.pidsamhai.covid19thailand.network.response.rapid.fuckyou.Static
import com.github.pidsamhai.covid19thailand.network.response.rapid.hell.Response

@Dao
interface StaticDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upSert(timeLine: Static)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upSertStatics(response: List<Response>)

    @Query("SELECT * FROM rapid_static LIMIT 1")
    fun getStatic(): LiveData<Static>

    @Query("SELECT * FROM rapid_statics WHERE responsePk = :pk")
    fun getStatics(pk: String): LiveData<Response>

    @Query("SELECT * FROM rapid_static WHERE pk = :country")
    fun getStaticReal(country: String): LiveData<Static>

    @Query("SELECT responsePk FROM rapid_statics")
    fun getCountries(): LiveData<List<String>>
}