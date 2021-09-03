package com.github.pidsamhai.covid19thailand.utilities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import timber.log.Timber
import java.util.*

const val APPWIDGET_REFRESH_REQ_CODE = 0
const val APPWIDGET_SETTING_REQ_CODE = 1
const val APPWIDGET_REFRESH_ACTION = "refresh"
const val APPWIDGET_EXTRA_REFRESH = "ex_refresh"
const val APPWIDGET_LAST_UPDATE_TEMPLATE = "%s/%s/%s"
const val APPWIDGET_TOTAL_TEMPLATE = "สะสม %,d"

@SuppressLint("UnspecifiedImmutableFlag")
fun refreshPendingIntent(context: Context?, clazz: Class<*>): PendingIntent {
    return PendingIntent.getBroadcast(
        context, APPWIDGET_REFRESH_REQ_CODE,
        Intent(context, clazz).apply {
            action = APPWIDGET_REFRESH_ACTION
        },
        0
    )
}

@SuppressLint("UnspecifiedImmutableFlag")
fun openWidgetConfigure(appWidgetId: Int, context: Context?, clazz: Class<*>): PendingIntent {
    Timber.d("id %s", appWidgetId)
    val settingIntent = Intent(context, clazz).apply {
        putExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            appWidgetId
        )
        putExtra(APPWIDGET_EXTRA_REFRESH, true)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        action = UUID.randomUUID().toString()
    }
    return PendingIntent.getActivity(
        context, APPWIDGET_SETTING_REQ_CODE,
        settingIntent,
        0
    )
}