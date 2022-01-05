package com.github.pidsamhai.covid19thailand.appwidget

import android.appwidget.AppWidgetManager
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.lifecycleScope
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.network.response.ddc.TodayByProvince
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class ThaiWidgetConfigureActivity : BaseWidgetConfigureActivity<TodayByProvince, String>() {
    private val vm: ThaiWidgetConfigureVM by viewModel()
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
                        listItem.indexOf(vm.getConfig(appWidgetId)?.location)
                    )
                    hideLoading()
                }
            }
        }
    }

    override fun onCreateClick() {
        if (selectedItem != SELECT_DEFAULT) {
            createWidget(listProvince[selectedItem] ?: return)
        } else {
            createTodayWidget()
        }
    }

    private fun createTodayWidget() {
        val config = WidgetConfig(location = "all", type = "thai")
        vm.saveWidgetSetting(appWidgetId, config)
        val appWidgetManager = GlanceAppWidgetManager(this)
        lifecycleScope.launchWhenCreated {
            val glanceId = appWidgetManager.getGlanceIds(ThaiAppWidget::class.java)
                .find { it.toString().contains("$appWidgetId") }
            if (glanceId == null) {
                finish()
            }

            vm.todayDataSource.collectLatest {
                when (it) {
                    is Result.Fail -> hideLoading()
                    Result.Initial -> Unit
                    Result.Loading -> showLoading()
                    is Result.Success -> {
                        ThaiAppWidget(today = it.data).update(
                            this@ThaiWidgetConfigureActivity,
                            glanceId!!
                        )
                        val resultValue = Intent()
                        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                        setResult(RESULT_OK, resultValue)
                        finish()
                    }
                }
            }
        }
    }

    override fun createWidget(data: TodayByProvince) {
        val config = WidgetConfig(location = data.province, type = "thai")
        vm.saveWidgetSetting(appWidgetId, config)

        vm.saveWidgetSetting(appWidgetId, config)
        val appWidgetManager = GlanceAppWidgetManager(this)
        lifecycleScope.launchWhenCreated {
            val glanceId = appWidgetManager.getGlanceIds(ThaiAppWidget::class.java)
                .find { it.toString().contains("$appWidgetId") }
            if (glanceId == null) {
                finish()
            }
            ThaiAppWidget(data).update(
                this@ThaiWidgetConfigureActivity,
                glanceId!!
            )
            val resultValue = Intent()
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            setResult(RESULT_OK, resultValue)
            finish()
        }
    }

    private fun MutableMap<String, TodayByProvince>.toProvinceItem(): MutableList<String> {
        val item = mutableListOf(
            SELECT_DEFAULT
        )
        item.addAll(this.keys)
        return item
    }

    companion object {
        const val SELECT_DEFAULT = "ทั้งประเทศ"
    }
}


