package com.github.pidsamhai.covid19thailand.appwidget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Datas
import com.github.pidsamhai.covid19thailand.repository.Repository
import com.github.pidsamhai.covid19thailand.utilities.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import org.koin.core.component.inject
import timber.log.Timber

class WorldWideAppWidgetProvider : BaseAppWidgetProvider<Static, String?>() {

    override val providerClass: Class<*> = WorldWideAppWidgetProvider::class.java
    private val repository: Repository by inject()

    override fun onWidgetUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        data: Static
    ) = updateWidget(
        context = context,
        appWidgetManager = appWidgetManager,
        appWidgetId = appWidgetId,
        data = data
    )

    override fun dataSource(setting: String?): Flow<Result<Static>>? {
        return repository.getStatic(setting ?: return null)
    }

    override fun getSetting(appWidgetId: Int): String? {
        return pref.getString(appWidgetId.toString(), null)
    }

    companion object {
        fun updateWidget(
            context: Context?,
            appWidgetManager: AppWidgetManager?,
            appWidgetId: Int,
            data: Static
        ) {
            Timber.d("Static %s", data.static)
            val static: Datas = data.static ?: return
            val view: RemoteViews = RemoteViews(
                context?.packageName,
                R.layout.today_app_widget
            ).apply {
                val country = if(static.country.lowercase() == "all") "World" else static.country
                setTextViewText(R.id.province, country)
                setTextViewText(R.id.newCase, static.cases?.new?.toInt().toCurrency())
                setTextViewText(R.id.latest_fetch, updateTime())
                setTextViewText(
                    R.id.totalCase,
                    static.cases?.total?.toCurrency()
                )
                setTextViewText(
                    R.id.latest_update,
                    static.time.toLastUpdate(APPWIDGET_LAST_UPDATE_TEMPLATE)
                )
                setOnClickPendingIntent(
                    R.id.btn_refresh,
                    refreshPendingIntent(
                        context,
                        WorldWideAppWidgetProvider::class.java
                    )
                )
                setOnClickPendingIntent(
                    R.id.btn_setting,
                    openWidgetConfigure(appWidgetId, context, WorldWidgetConfigureActivity::class.java)
                )
            }
            appWidgetManager?.updateAppWidget(appWidgetId, view)
        }
    }
}