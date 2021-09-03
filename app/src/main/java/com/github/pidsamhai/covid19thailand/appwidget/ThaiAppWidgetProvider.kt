package com.github.pidsamhai.covid19thailand.appwidget

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.network.response.ddc.TodayByProvince
import com.github.pidsamhai.covid19thailand.repository.Repository
import com.github.pidsamhai.covid19thailand.utilities.*
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.inject


class ThaiAppWidgetProvider : BaseAppWidgetProvider<TodayByProvince, String>() {

    private val repository: Repository by inject()
    override val providerClass: Class<*> = ThaiAppWidgetProvider::class.java

    override fun onWidgetUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        data: TodayByProvince
    ) = updateWidget(
        context = context,
        appWidgetManager = appWidgetManager,
        appWidgetId = appWidgetId,
        data = data
    )


    override fun dataSource(setting: String?): Flow<Result<TodayByProvince>>? {
        return repository.getTodayByProvince(setting ?: return null)
    }

    override fun getSetting(appWidgetId: Int): String? {
        return pref.getString(appWidgetId.toString(), null)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    companion object {
        fun updateWidget(
            context: Context?,
            appWidgetManager: AppWidgetManager?,
            appWidgetId: Int,
            data: TodayByProvince
        ) {
            val view: RemoteViews = RemoteViews(
                context?.packageName,
                R.layout.today_app_widget
            ).apply {
                setTextViewText(R.id.province, data.province)
                setTextViewText(R.id.newCase, data.newCase.toCurrency())
                setTextViewText(R.id.latest_fetch, updateTime())
                setTextViewText(
                    R.id.totalCase,
                    data.totalCase.toCurrency()
                )
                setTextViewText(
                    R.id.latest_update,
                    data.updateDate.toLastUpdate(APPWIDGET_LAST_UPDATE_TEMPLATE)
                )
                setOnClickPendingIntent(
                    R.id.btn_refresh,
                    refreshPendingIntent(context, ThaiAppWidgetProvider::class.java)
                )
                setOnClickPendingIntent(
                    R.id.btn_setting,
                    openWidgetConfigure(appWidgetId, context, ThaiAppWidgetProvider::class.java)
                )
            }
            appWidgetManager?.updateAppWidget(appWidgetId, view)
        }
    }
}