package com.github.pidsamhai.covid19thailand.appwidget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.content.edit
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.network.response.ddc.TodayByProvince
import com.github.pidsamhai.covid19thailand.repository.Repository
import com.github.pidsamhai.covid19thailand.utilities.toCurrency
import com.github.pidsamhai.covid19thailand.utilities.toLastUpdate
import com.github.pidsamhai.covid19thailand.utilities.updateTime
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.util.*


class TodayAppWidgetProvider : AppWidgetProvider(), KoinComponent {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val pref: SharedPreferences by inject(named("widgetPref"))
    private val repository: Repository by inject()
    private var job: Job? = null

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)

        when (intent?.action) {
            REFRESH_ACTION -> {
                Toast.makeText(context, "Updating...", Toast.LENGTH_SHORT).show()
                val updateIntent = Intent(context, javaClass)
                updateIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

                val ids = AppWidgetManager.getInstance(context)
                    .getAppWidgetIds(
                        ComponentName(
                            context,
                            javaClass
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
                repository.getTodayByProvince(
                    getProvince(appWidgetId) ?: return@launch
                ).last().also { result ->
                    when (result) {
                        is Result.Success -> {
                            updateWidget(
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

    private fun getProvince(widgetId: Int): String? {
        return pref.getString(widgetId.toString(), null)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    companion object {
        private const val REFRESH_REQ_CODE = 0
        private const val SETTING_REQ_CODE = 1
        private const val REFRESH_ACTION = "refresh"
        const val EXTRA_REFRESH = "ex_refresh"
        private const val LAST_UPDATE_TEMPLATE = "ข้อมูลล่าสุด %s"

        fun updateWidget(
            context: Context?,
            appWidgetManager: AppWidgetManager?,
            appWidgetId: Int,
            todayByProvince: TodayByProvince
        ) {
            val view: RemoteViews = RemoteViews(
                context?.packageName,
                R.layout.today_app_widget
            ).apply {
                setTextViewText(R.id.province, todayByProvince.province)
                setTextViewText(R.id.newCase, todayByProvince.newCase.toCurrency())
                setTextViewText(R.id.latest_fetch, updateTime())
                setTextViewText(
                    R.id.totalCase,
                    todayByProvince.totalCase.toCurrency()
                )
                setTextViewText(
                    R.id.latest_update,
                    todayByProvince.updateDate.toLastUpdate(LAST_UPDATE_TEMPLATE)
                )
                setOnClickPendingIntent(
                    R.id.btn_refresh,
                    refreshPendingIntent(context, TodayAppWidgetProvider::class.java)
                )
                setOnClickPendingIntent(
                    R.id.btn_setting,
                    openWidgetConfigure(appWidgetId, context)
                )
            }
            appWidgetManager?.updateAppWidget(appWidgetId, view)
        }

        private fun refreshPendingIntent(context: Context?, clazz: Class<*>): PendingIntent {
            return PendingIntent.getBroadcast(
                context, REFRESH_REQ_CODE,
                Intent(context, clazz).apply {
                    action = REFRESH_ACTION
                },
                0
            )
        }

        private fun openWidgetConfigure(appWidgetId: Int, context: Context?): PendingIntent {
            val settingIntent = Intent(context, WidgetConfigureActivity::class.java).apply {
                putExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    appWidgetId
                )
                putExtra(EXTRA_REFRESH, true)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                action = UUID.randomUUID().toString()
            }
            return PendingIntent.getActivity(
                context, SETTING_REQ_CODE,
                settingIntent,
                0
            )
        }
    }
}