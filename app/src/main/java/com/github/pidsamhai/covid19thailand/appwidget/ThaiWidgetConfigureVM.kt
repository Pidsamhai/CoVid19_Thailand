package com.github.pidsamhai.covid19thailand.appwidget

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import com.github.pidsamhai.covid19thailand.network.response.ddc.TodayByProvince
import com.github.pidsamhai.covid19thailand.repository.Repository
import com.github.pidsamhai.covid19thailand.utilities.getWidgetConfig
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class ThaiWidgetConfigureVM(
    repository: Repository,
    private val pref: SharedPreferences
) : ViewModel() {

    val dataSource: Flow<Result<List<TodayByProvince>>> = repository.getTodayByProvinces(true)
    val todayDataSource: Flow<Result<Today>> = repository.getTodayFlow()

    fun saveWidgetSetting(widgetId: Int, config: WidgetConfig) = pref.edit {
        putString(widgetId.toString(), Gson().toJson(config))
    }

    fun getConfig(widgetId: Int): WidgetConfig? {
        Timber.d("GetConfig %s", widgetId.toString())
        return pref.getWidgetConfig(widgetId)
    }

}