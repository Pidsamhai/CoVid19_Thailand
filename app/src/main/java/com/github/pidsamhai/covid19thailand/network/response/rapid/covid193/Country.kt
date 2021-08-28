package com.github.pidsamhai.covid19thailand.network.response.rapid.covid193

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "counties")
data class Country(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "name")
    val name: String
)