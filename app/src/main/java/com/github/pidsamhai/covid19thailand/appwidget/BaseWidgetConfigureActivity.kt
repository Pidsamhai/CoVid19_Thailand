package com.github.pidsamhai.covid19thailand.appwidget

import android.appwidget.AppWidgetManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.lifecycleScope
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.network.response.ddc.TodayByProvince
import com.github.pidsamhai.covid19thailand.utilities.APPWIDGET_EXTRA_REFRESH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

abstract class BaseWidgetConfigureActivity<T : Any, S : Any?> : AppCompatActivity() {
    protected val adapter: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, mutableListOf())
    }
    protected var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    protected abstract var listItem: MutableList<String>
    protected var selectedItem: String = ""
    protected lateinit var btnCreate: Button
    protected lateinit var spinner: AppCompatSpinner
    private val loadingDialog: AlertDialog by lazy {
        LoadingDialog(this)
    }

    protected abstract fun createWidget(data: T)
    protected abstract suspend fun observeData()
    protected abstract fun onCreateClick()
    protected open fun onSelectedItem(item: String) = Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.widget_config)
        btnCreate = findViewById(R.id.btn_create)
        spinner = findViewById(R.id.spinner_province)

        supportActionBar?.title = "Create widget"

        setResult(RESULT_CANCELED)


        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
            if (extras.getBoolean(APPWIDGET_EXTRA_REFRESH, false)) {
                btnCreate.text = "Update"
            }
        }

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }

        Timber.d("Prepare Create widget extras ${intent.extras?.keySet()?.map { intent.extras?.get(it) }}")
        Timber.d("Prepare Create widget id $appWidgetId")

        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item = parent?.selectedItem as String
                if (item != selectedItem) {
                    selectedItem = item
                    onSelectedItem(selectedItem)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        btnCreate.setOnClickListener {
            onCreateClick()
        }

        lifecycleScope.launchWhenResumed {
            observeData()
        }
    }

    protected fun showLoading() = lifecycleScope.launch(Dispatchers.Main) { loadingDialog.show() }

    protected fun hideLoading() = lifecycleScope.launch(Dispatchers.Main) { loadingDialog.dismiss() }
}


