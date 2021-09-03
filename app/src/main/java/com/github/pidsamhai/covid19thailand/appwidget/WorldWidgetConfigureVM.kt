package com.github.pidsamhai.covid19thailand.appwidget

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.network.response.ddc.TodayByProvince
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import com.github.pidsamhai.covid19thailand.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
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

    fun saveWidgetSetting(widgetId: Int, setting: String) = pref.edit {
        putString(widgetId.toString(), setting)
    }

    fun getSetting(widgetId: Int): String? {
        Timber.d("GetSetting %s", widgetId.toString())
        return pref.getString(widgetId.toString(), null)
    }

}