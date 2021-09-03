package com.github.pidsamhai.covid19thailand.appwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.edit
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.utilities.APPWIDGET_REFRESH_ACTION
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named


abstract class BaseAppWidgetProvider<T : Any, S : Any?> : AppWidgetProvider(), KoinComponent {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var job: Job? = null
    protected val pref: SharedPreferences by inject(named("widgetPref"))
    protected abstract val providerClass:Class<*>

    abstract fun onWidgetUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        data: T
    )

    abstract fun dataSource(setting: S?): Flow<Result<T>>?
    abstract fun getSetting(appWidgetId: Int): S?

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)

        when (intent?.action) {
            APPWIDGET_REFRESH_ACTION -> {
                Toast.makeText(context, "Updating...", Toast.LENGTH_SHORT).show()
                val updateIntent = Intent(context, javaClass)
                updateIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

                val ids = AppWidgetManager.getInstance(context)
                    .getAppWidgetIds(
                        ComponentName(
                            context,
                            providerClass
                        )
                    )

                updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                context.sendBroadcast(updateIntent)
            }
        }
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        job = scope.launch {
            appWidgetIds?.forEach { appWidgetId ->
                dataSource(getSetting(appWidgetId))?.collectLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            onWidgetUpdate(
                                context,
                                appWidgetManager,
                                appWidgetId,
                                result.data
                            )
                            job?.cancel()
                            job = null
                        }
                    }
                }
            }
        }
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        scope.cancel()
        pref.edit {
            remove(appWidgetIds?.first().toString())
        }
        super.onDeleted(context, appWidgetIds)
    }
}