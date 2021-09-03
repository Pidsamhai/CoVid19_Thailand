package com.github.pidsamhai.covid19thailand.appwidget

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.network.response.ddc.TodayByProvince
import com.github.pidsamhai.covid19thailand.repository.Repository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class TodayByProvinceWidgetConfigureVM(
    repository: Repository,
    private val pref: SharedPreferences
) : ViewModel() {

    val dataSource: Flow<Result<List<TodayByProvince>>> = repository.getTodayByProvinces(true)

    fun saveWidgetSetting(widgetId: Int, setting: String) = pref.edit {
        putString(widgetId.toString(), setting)
    }

    fun getSetting(widgetId: Int): String? {
        Timber.d("GetSetting %s", widgetId.toString())
        return pref.getString(widgetId.toString(), null)
    }

}