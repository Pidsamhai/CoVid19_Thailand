package com.github.pidsamhai.covid19thailand.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Country
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Datas
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.history.History
import kotlinx.coroutines.flow.Flow

@Dao
interface RapidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upSert(timeLine: Static)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upSertStatics(datas: List<Datas>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upSertHistories(history: List<History>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upSertCountries(countries: List<Country>)

    @Query("SELECT * FROM rapid_history WHERE historyPk LIKE '%' || :country || '%' ORDER BY time asc")
    fun getHistory(country: String): LiveData<List<History>>

    @Query("DELETE FROM rapid_history WHERE historyPk LIKE '%' || :country || '%' ")
    fun dropHistory(country: String)

    @Query("SELECT * FROM rapid_statics WHERE responsePk = :country")
    fun getStatic(country: String): LiveData<Datas>

    @Query("SELECT * FROM rapid_statics WHERE responsePk = :pk")
    fun getStatics(pk: String): LiveData<Datas>

    @Query("SELECT * FROM rapid_static WHERE pk = :country")
    fun getStaticReal(country: String): LiveData<Static>

    @Query("SELECT * FROM rapid_static WHERE pk = :country")
    fun getStaticX(country: String): Flow<Static?>

    @Query("SELECT * FROM rapid_statics")
    fun getStaticsX(): List<Datas>

    @Query("SELECT name FROM counties ORDER BY name ASC")
    fun getCountries(): Flow<List<String>>
}