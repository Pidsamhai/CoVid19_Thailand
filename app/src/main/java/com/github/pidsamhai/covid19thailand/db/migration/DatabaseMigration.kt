package com.github.pidsamhai.covid19thailand.db.migration

import androidx.room.ColumnInfo
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.annotations.SerializedName
import org.intellij.lang.annotations.Language
import timber.log.Timber

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE rapid_countries")
    }
}
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
//        @ColumnInfo(name = "newCase")
//        @SerializedName("new_case")
//        val newCase: Int?,
//        @ColumnInfo(name = "newCaseExcludeAbroad")
//        @SerializedName("new_case_excludeabroad")
//        val newCaseExcludeAbroad: Int?,
//        @ColumnInfo(name = "totalCase")
//        @SerializedName("total_case")
//        val totalCase: Int?,
//        @ColumnInfo(name = "totalCaseExcludeAbroad")
//        @SerializedName("total_case_excludeabroad")
//        val totalCaseExcludeAbroad: Int?,
//        @ColumnInfo(name = "txnDate")
//        @SerializedName("txn_date")
//        val txnDate: String?,
        database.execSQL("DROP TABLE today")
        database.execSQL(
            """
                CREATE TABLE today(
                 updateDate TEXT PRIMARY KEY NOT NULL,
                 newCase INTEGER,
                 newCaseExcludeAbroad INTEGER,
                 totalCase INTEGER,
                 totalCaseExcludeAbroad INTEGER,
                 txnDate TEXT
                );
            """.trimIndent()
        )
    }
}

fun <T : RoomDatabase> RoomDatabase.Builder<T>.addMigrations(list: List<Migration>): RoomDatabase.Builder<T> {
    list.map { addMigrations(it) }
    return this
}

val MIGRATIONS = listOf(
    MIGRATION_3_4,
    MIGRATION_4_5
)