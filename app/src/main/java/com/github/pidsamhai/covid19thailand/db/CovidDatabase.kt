package com.github.pidsamhai.covid19thailand.db

import android.content.Context
import androidx.room.*
import com.github.pidsamhai.covid19thailand.db.TypeConverter
import com.github.pidsamhai.covid19thailand.network.response.TimeLine
import com.github.pidsamhai.covid19thailand.network.response.Today
import com.github.pidsamhai.covid19thailand.utilities.DATABASE_NAME

@Database(
    entities = [Today::class,TimeLine::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class CovidDatabase : RoomDatabase(){
    abstract fun todayDao():TodayDao

    companion object {
        @Volatile private var instance:CovidDatabase? = null
        fun getInstance(context: Context):CovidDatabase{
            return instance ?: synchronized(this){
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context):CovidDatabase{
            return Room.databaseBuilder(context,CovidDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}