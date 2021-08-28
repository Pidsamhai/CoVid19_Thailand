package com.github.pidsamhai.covid19thailand.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.pidsamhai.covid19thailand.db.dao.RapidDao
import com.github.pidsamhai.covid19thailand.db.dao.TimeLineDao
import com.github.pidsamhai.covid19thailand.db.dao.TodayByProvinceDao
import com.github.pidsamhai.covid19thailand.db.dao.TodayDao
import com.github.pidsamhai.covid19thailand.db.migration.MIGRATIONS
import com.github.pidsamhai.covid19thailand.db.migration.addMigrations
import com.github.pidsamhai.covid19thailand.network.response.ddc.Data
import com.github.pidsamhai.covid19thailand.network.response.ddc.TimeLine
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import com.github.pidsamhai.covid19thailand.network.response.ddc.TodayByProvince
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Country
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Datas
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.history.History
import com.github.pidsamhai.covid19thailand.utilities.DATABASE_NAME

@Database(
    entities = [
        Today::class,
        TimeLine::class,
        Data::class,
        Static::class,
        Datas::class,
        History::class,
        TodayByProvince::class,
        Country::class
    ],
    autoMigrations = [
        AutoMigration(from = 5, to = 6),
    ],
    version = 6,
    exportSchema = true
)
@TypeConverters(TypeConverter::class)
abstract class CoVid19Database : RoomDatabase() {
    abstract val todayDao: TodayDao
    abstract val timeLineDao: TimeLineDao
    abstract val rapidDao: RapidDao
    abstract val todayByProvince: TodayByProvinceDao

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
                .addMigrations(MIGRATIONS)
                .build()
        }
    }
}