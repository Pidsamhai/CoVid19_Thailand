package com.github.pidsamhai.covid19thailand.appwidget

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.core.content.edit
import androidx.glance.LocalGlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.github.pidsamhai.covid19thailand.R
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class WorldGlanceAppwidgetProvider : GlanceAppWidgetReceiver(), KoinComponent {
    private val pref: SharedPreferences by inject(named("widgetPref"))
    override val glanceAppWidget: GlanceAppWidget
        get() = WorldAppWidget()

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        pref.edit {
            remove(appWidgetIds.first().toString())
        }
        super.onDeleted(context, appWidgetIds)
    }
}

class WorldAppWidget(private val data: Static? = null) : GlanceAppWidget(R.layout.app_widget_2_col) {
    @Composable
    override fun Content() {
        return if (data != null) {
            ContentWidget(static = data)
        } else {
            EmptyContent()
        }
    }
}


