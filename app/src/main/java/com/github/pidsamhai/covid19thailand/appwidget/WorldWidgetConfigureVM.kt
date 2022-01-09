package com.github.pidsamhai.covid19thailand.appwidget

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import com.github.pidsamhai.covid19thailand.repository.Repository
import com.github.pidsamhai.covid19thailand.utilities.getWidgetConfig
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class WorldWidgetConfigureVM(
    private val repository: Repository,
    private val pref: SharedPreferences
) : ViewModel() {

    val countries: Flow<Result<List<String>>> = repository.getCountries()

    fun getCountry(country: String): Flow<Result<Static>> {
        return repository.getStatic(country)
    }

    fun saveWidgetSetting(widgetId: Int, config: WidgetConfig) = pref.edit {
        putString(widgetId.toString(), Gson().toJson(config))
    }

    fun getConfig(widgetId: Int): WidgetConfig? {
        Timber.d("GetConfig %s", widgetId.toString())
        return pref.getWidgetConfig(widgetId)
    }

}