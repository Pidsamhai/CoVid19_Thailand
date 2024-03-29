package com.github.pidsamhai.covid19thailand.appwidget

import android.appwidget.AppWidgetManager
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.lifecycleScope
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WorldWidgetConfigureActivity : BaseWidgetConfigureActivity<Static, String>() {
    private val vm: WorldWidgetConfigureVM by viewModel()
    override var listItem: MutableList<String> = mutableListOf()
    private var selectedCountry: Static? = null

    override suspend fun observeData() {
        vm.countries.collectLatest {
            when (it) {
                is Result.Fail -> hideLoading()
                Result.Loading -> showLoading()
                is Result.Success -> {
                    listItem = it.data.toListItem()
                    adapter.addAll(listItem)
                    adapter.notifyDataSetChanged()
                    spinner.setSelection(
                        listItem.indexOf(vm.getConfig(appWidgetId)?.location)
                    )
                }
                else -> Unit
            }
        }
    }

    override fun onCreateClick() {
        selectedCountry?.let {
            createWidget(it)
        }
    }

    override fun onSelectedItem(item: String) {
        if (item == SELECT_DEFAULT) return
        selectedCountry = null
        lifecycleScope.launch(Dispatchers.IO) {
            vm.getCountry(item).collectLatest {
                when (it) {
                    is Result.Fail -> hideLoading()
                    Result.Loading -> showLoading()
                    is Result.Success -> {
                        selectedCountry = it.data
                        hideLoading()
                    }
                }
            }
        }
    }

    override fun createWidget(data: Static) {
        val country = data.static?.country ?: return
        val config = WidgetConfig(location = country, type = "world")
        vm.saveWidgetSetting(appWidgetId, config)
        val appWidgetManager = GlanceAppWidgetManager(this)
        lifecycleScope.launchWhenCreated {
            val glanceId = appWidgetManager.getGlanceIds(WorldAppWidget::class.java)
                .find { it.toString().contains("$appWidgetId") }
            if (glanceId == null) {
                finish()
            }
            WorldAppWidget(data).update(
                this@WorldWidgetConfigureActivity.baseContext,
                glanceId!!
            )
            val resultValue = Intent()
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            setResult(RESULT_OK, resultValue)
            finish()
        }
    }

    private fun Collection<String>.toListItem(): MutableList<String> {
        val item = mutableListOf(
            SELECT_DEFAULT,
            "ALL"
        )
        item.addAll(this)
        return item
    }

    companion object {
        private const val SELECT_DEFAULT = "Please selected country"
    }
}


