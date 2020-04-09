package com.github.pidsamhai.covid19thailand.utilities

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.pidsamhai.covid19thailand.network.response.Data
import com.github.pidsamhai.covid19thailand.network.response.Today
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.androidx.viewmodel.ViewModelParameter
import org.koin.androidx.viewmodel.koin.getViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent.getKoin
import java.lang.reflect.Type

const val DATABASE_NAME = "covid19-db"

fun String.toToday() : Today {
//    val type: Type = object : TypeToken<Today>() {}.type
    return Gson().fromJson(this,Today::class.java)
}

fun StringForMetter(data:ArrayList<String>) : ValueFormatter{
    return object : ValueFormatter(){
        override fun getFormattedValue(value: Float): String {
            return data[value.toInt()]
        }
    }
}

fun newS(data: Int?): String {
    return "( เพิ่มขึ้น $data )"
}

fun lastUpdate(data: String): String {
    return "( อัพเดทล่าสุด $data)"
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
