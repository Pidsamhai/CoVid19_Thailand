package com.github.pidsamhai.covid19thailand.utilities

import android.content.SharedPreferences
import androidx.glance.GlanceId
import com.github.pidsamhai.covid19thailand.appwidget.WidgetConfig
import com.google.gson.Gson

fun Int?.toCurrency(template: String = "%,d"): String {
    return if (this == null) ""
    else template.format(this)
}

fun GlanceId.widgetId(): Int? {
    return "[\\d]+".toRegex().find(this.toString())?.value?.toInt()
}

fun SharedPreferences.getWidgetConfig(widgetId: Int): WidgetConfig? {
    return try {
        val config = this.getString(widgetId.toString(), null)
        Gson().fromJson(config, WidgetConfig::class.java)
    } catch (e: Exception) {
        null
    }
}