package com.github.pidsamhai.covid19thailand.appwidget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.lifecycleScope
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.network.response.ddc.TodayByProvince
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class WidgetConfigureActivity : AppCompatActivity() {
    private val vm: WidgetConfigureVm by viewModel()
    private val adapter: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, mutableListOf("FUCK"))
    }
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private val listProvince: MutableMap<String, TodayByProvince> = mutableMapOf()
    private var listItem: MutableList<String> = mutableListOf()
    private var selectedProvince: String = SELECT_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.widget_config)

        supportActionBar?.title = "Create widget"

        setResult(RESULT_CANCELED)
        val btnCreate = findViewById<Button>(R.id.btn_create)
        val spinner = findViewById<AppCompatSpinner>(R.id.spinner_province)

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
            if (extras.getBoolean(TodayAppWidgetProvider.EXTRA_REFRESH, false)) {
                btnCreate.text = "Update"
            }
        }

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }

        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedProvince = parent?.selectedItem as String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        btnCreate.setOnClickListener {
            if (selectedProvince != SELECT_DEFAULT) {
                createWidget(listProvince[selectedProvince] ?: return@setOnClickListener)
            }
        }

        lifecycleScope.launchWhenResumed {
            vm.todayProvinceResource.collectLatest {
                when (it) {
                    Result.Fail -> {}
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
    }

    private fun createWidget(todayByProvince: TodayByProvince) {
        vm.saveWidgetParams(appWidgetId, todayByProvince.province)
        val appWidgetManager = AppWidgetManager.getInstance(this)
        TodayAppWidgetProvider.updateWidget(
            this,
            appWidgetManager,
            appWidgetId,
            todayByProvince
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


