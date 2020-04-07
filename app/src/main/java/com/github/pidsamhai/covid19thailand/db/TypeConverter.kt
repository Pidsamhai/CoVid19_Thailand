package com.github.pidsamhai.covid19thailand.db

import androidx.room.TypeConverter
import com.github.pidsamhai.covid19thailand.network.response.Data
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class TypeConverter {
    @TypeConverter
    fun fromString(value: String): List<Data> {
        val listType: Type = object : TypeToken<ArrayList<Data>>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromList(list: List<Data>):String {
        return Gson().toJson(list)
    }
}