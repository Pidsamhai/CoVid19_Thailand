package com.github.pidsamhai.covid19thailand.utilities

import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Datas
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.lang.reflect.Type

const val DATABASE_NAME = "covid19-db"
const val OWNER = " Pidsamhai"
const val REPOSITORY = "CoVid19_Thailand"

fun String.toToday() : Today {
    return Gson().fromJson(this,
        Today::class.java)
}

fun String.toDatas() : Datas {
    return Gson().fromJson(this,
        Datas::class.java)
}

fun StringForMetter(data:ArrayList<String>) : ValueFormatter{
    Timber.e(data.toString())
    return object : ValueFormatter(){
        override fun getFormattedValue(value: Float): String {
            return data[value.toInt()]
        }
    }
}

fun newS(data: Int?): String {
    return "( เพิ่มขึ้น ${data.toCurrency()} )"
}

fun lastUpdate(data: String): String {
    return "( อัพเดทล่าสุด $data)"
}

fun String?.toLastUpdate(): String? {
    if (this != null) return "( อัพเดทล่าสุด $this )"
    return this
}

fun String.toToDay(value: String): Today {
    val listType: Type = object : TypeToken<Today>() {}.type
    return Gson().fromJson(value, listType)
}

fun List<String>.addEmptyFirst(): List<String> {
    val mutableList = mutableListOf("")
    mutableList.addAll(this)
    return mutableList
}

object Keys {
    init {
        System.loadLibrary("native-lib")
    }
    external fun rapidCovid19Api(): String
}
