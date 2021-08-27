package com.github.pidsamhai.covid19thailand.appwidget

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.github.pidsamhai.covid19thailand.repository.Repository
import timber.log.Timber

class WidgetConfigureVm(
    private val repository: Repository,
    private val pref: SharedPreferences
): ViewModel() {

    val todayProvinceResource = repository.getTodayByProvinces(true)

    fun getSetting(appWidgetId: Int): String? {
        val setting = pref.getString(appWidgetId.toString(), null)
        Timber.d("id $appWidgetId $setting")
        return setting
    }

    fun saveWidgetParams(widgetId: Int, province: String) = pref.edit {
        putString(widgetId.toString(), province)
    }

}