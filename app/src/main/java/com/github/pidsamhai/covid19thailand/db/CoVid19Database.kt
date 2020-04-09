package com.github.pidsamhai.covid19thailand.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.pidsamhai.covid19thailand.db.dao.CountriesDao
import com.github.pidsamhai.covid19thailand.db.dao.StaticDao
import com.github.pidsamhai.covid19thailand.db.dao.TimeLineDao
import com.github.pidsamhai.covid19thailand.db.dao.TodayDao
import com.github.pidsamhai.covid19thailand.network.response.Data
import com.github.pidsamhai.covid19thailand.network.response.TimeLine
import com.github.pidsamhai.covid19thailand.network.response.Today
import com.github.pidsamhai.covid19thailand.network.response.rapid.country.Countries
import com.github.pidsamhai.covid19thailand.network.response.rapid.fuckyou.Static
import com.github.pidsamhai.covid19thailand.network.response.rapid.hell.Response
import com.github.pidsamhai.covid19thailand.utilities.DATABASE_NAME

@Database(
    entities = [Today::class, TimeLine::class, Data::class, Static::class, Countries::class, Response::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class CoVid19Database : RoomDatabase() {
    abstract fun todayDao(): TodayDao
    abstract fun timeLineDao(): TimeLineDao
    abstract fun staticDao(): StaticDao
    abstract fun countriesDao(): CountriesDao

    companion object {
        @Volatile
        private var instance: CoVid19Database? = null
        fun getInstance(context: Context): CoVid19Database {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): CoVid19Database {
            return Room.databaseBuilder(context, CoVid19Database::class.java, DATABASE_NAME)
                .build()
        }
    }
}