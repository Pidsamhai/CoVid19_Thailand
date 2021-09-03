package com.github.pidsamhai.covid19thailand.appwidget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.lifecycleScope
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.network.response.ddc.TodayByProvince
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class WidgetConfigureActivity : BaseWidgetConfigureActivity<TodayByProvince, String>() {
    private val vm: TodayByProvinceWidgetConfigureVM by viewModel()
    private val listProvince: MutableMap<String, TodayByProvince> = mutableMapOf()
    override var listItem: MutableList<String> = mutableListOf()

    override suspend fun observeData() {
        vm.dataSource.collectLatest {
            when (it) {
                is Result.Fail -> {}
                Result.Loading -> {}
                is Result.Success -> {
                    it.data.forEach { m ->
                        listProvince[m.province] = m
                    }
                    listItem = listProvince.toProvinceItem()
                    adapter.clear()
                    adapter.addAll(listItem)
                    adapter.notifyDataSetChanged()
                    spinner.setSelection(
                        listItem.indexOf(vm.getSetting(appWidgetId))
                    )
                }
            }
        }
    }

    override fun onCreateClick() {
        if (selectedItem != SELECT_DEFAULT) {
            createWidget(listProvince[selectedItem] ?: return)
        }
    }

    override fun createWidget(data: TodayByProvince) {
        vm.saveWidgetSetting(appWidgetId, data.province)
        val appWidgetManager = AppWidgetManager.getInstance(this)
        TodayAppWidgetProvider.updateWidget(
            this,
            appWidgetManager,
            appWidgetId,
            data
        )
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

    private fun MutableMap<String, TodayByProvince>.toProvinceItem(): MutableList<String> {
        val item = mutableListOf(
            SELECT_DEFAULT
        )
        item.addAll(this.keys)
        return item
    }

    companion object {
        private const val SELECT_DEFAULT = "ทั้งประเทศ"
    }
}


