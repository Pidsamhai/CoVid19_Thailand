package com.github.pidsamhai.covid19thailand.appwidget

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.core.content.edit
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import com.github.pidsamhai.covid19thailand.network.response.ddc.TodayByProvince
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class ThaiGlanceAppwidgetProvider : GlanceAppWidgetReceiver(), KoinComponent {
    private val pref: SharedPreferences by inject(named("widgetPref"))
    override val glanceAppWidget: GlanceAppWidget
        get() = ThaiAppWidget()

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        pref.edit {
            remove(appWidgetIds.first().toString())
        }
        super.onDeleted(context, appWidgetIds)
    }
}

class ThaiAppWidget(private val data: TodayByProvince? = null, private val today: Today? = null) :
    GlanceAppWidget(R.layout.app_widget_2_col) {
    @Composable
    override fun Content() {
        return if (data != null) {
            ContentWidget(data = data)
        } else if (today != null) {
            ContentWidget(today = today)
        } else {
            EmptyContent()
        }
    }
}


