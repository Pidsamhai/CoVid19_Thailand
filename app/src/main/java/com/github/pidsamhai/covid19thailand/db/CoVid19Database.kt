package com.github.pidsamhai.covid19thailand.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.pidsamhai.covid19thailand.db.dao.RapidDao
import com.github.pidsamhai.covid19thailand.db.dao.TimeLineDao
import com.github.pidsamhai.covid19thailand.db.dao.TodayDao
import com.github.pidsamhai.covid19thailand.db.migration.MIGRATION_3_4
import com.github.pidsamhai.covid19thailand.network.response.ddc.Data
import com.github.pidsamhai.covid19thailand.network.response.ddc.TimeLine
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Datas
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.history.History
import com.github.pidsamhai.covid19thailand.utilities.DATABASE_NAME

@Database(
    entities = [Today::class, TimeLine::class, Data::class, Static::class, Datas::class,History::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class CoVid19Database : RoomDatabase() {
    abstract fun todayDao(): TodayDao
    abstract fun timeLineDao(): TimeLineDao
    abstract fun rapidDao(): RapidDao

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
                .addMigrations(MIGRATION_3_4)
                .build()
        }
    }
}