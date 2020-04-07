package com.github.pidsamhai.covid19thailand.utilities

import com.github.pidsamhai.covid19thailand.network.response.Today
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

const val DATABASE_NAME = "covid19-db"

fun String.toTOday() : Today {
//    val type: Type = object : TypeToken<Today>() {}.type
    return Gson().fromJson(this,Today::class.java)
}