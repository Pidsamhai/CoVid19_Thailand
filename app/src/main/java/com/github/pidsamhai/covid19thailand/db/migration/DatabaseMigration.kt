package com.github.pidsamhai.covid19thailand.db.migration

import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE rapid_countries")
    }
}
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
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

val MIGRATION_5_6 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
                CREATE TABLE IF NOT EXISTS `countries` (`name` TEXT NOT NULL, PRIMARY KEY(`name`));
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
    MIGRATION_4_5,
    MIGRATION_5_6,
)