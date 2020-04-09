package com.github.pidsamhai.covid19thailand.db

import androidx.room.TypeConverter
import com.github.pidsamhai.covid19thailand.network.response.Data
import com.github.pidsamhai.covid19thailand.network.response.rapid.fuckyou.*
import com.github.pidsamhai.covid19thailand.network.response.rapid.hell.Response
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class TypeConverter {
    @TypeConverter
    fun fromStringData(value: String): List<Data> {
        val listType: Type = object : TypeToken<ArrayList<Data>>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromListData(list: List<Data>):String {
        return Gson().toJson(list)
    }
    @TypeConverter
    fun fromStringResponse(value: String): List<Response> {
        val listType: Type = object : TypeToken<List<Response>>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromListResponse(list: List<Response>):String {
        return Gson().toJson(list)
    }
    @TypeConverter
    fun fromStringError(value: String): List<String> {
        val listType: Type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromListString(list: List<String>):String {
        return Gson().toJson(list)
    }
    @TypeConverter
    fun fromStringParameter(value: String): Parameters {
        val listType: Type = object : TypeToken<Parameters>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromListString(list: Parameters):String {
        return Gson().toJson(list)
    }
    @TypeConverter
    fun fromStringCases(value: String): Cases {
        val listType: Type = object : TypeToken<Cases>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromCasesString(list: Cases):String {
        return Gson().toJson(list)
    }
    @TypeConverter
    fun fromStringDeaths(value: String): Deaths {
        val listType: Type = object : TypeToken<Deaths>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromDeathsList(list: Deaths):String {
        return Gson().toJson(list)
    }
    @TypeConverter
    fun fromStringTests(value: String): Tests {
        val listType: Type = object : TypeToken<Tests>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromTestsList(list: Tests):String {
        return Gson().toJson(list)
    }
}