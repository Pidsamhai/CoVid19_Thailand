package com.github.pidsamhai.covid19thailand.appwidget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import androidx.glance.GlanceId
import androidx.glance.action.Action
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.repository.Repository
import com.github.pidsamhai.covid19thailand.ui.MainActivity
import com.github.pidsamhai.covid19thailand.utilities.APPWIDGET_EXTRA_REFRESH
import com.github.pidsamhai.covid19thailand.utilities.getWidgetConfig
import com.github.pidsamhai.covid19thailand.utilities.widgetId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import timber.log.Timber

inline fun<reified T: Activity> configureIntent(glanceId: GlanceId, context: Context): Intent {
    val configureIntent = Intent(context, T::class.java).apply {
        putExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            glanceId.widgetId()
        )
        putExtra(APPWIDGET_EXTRA_REFRESH, true)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    return configureIntent
}

inline fun <reified T: Activity> actionStartConfigureActivity(glanceId: GlanceId, context: Context): Action {
    return actionStartActivity(configureIntent<T>(glanceId, context))
}

//class ManualUpdateWorldWidgetCallBack : ActionCallback, KoinComponent {
//    private val pref: SharedPreferences by inject(named("widgetPref"))
//    private val repository: Repository by inject()
//    override suspend fun onRun(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
//        withContext(Dispatchers.Main) {
//            Toast.makeText(context, "Updating...", Toast.LENGTH_SHORT).show()
//        }
//        val config = pref.getWidgetConfig(glanceId.widgetId()!!)!!
//        Timber.d("GlanceId $glanceId regexId")
//        repository.getStatic(config.location).collectLatest {
//            when (it) {
//                is Result.Success -> WorldAppWidget(it.data).update(context, glanceId)
//                else -> Unit
//            }
//        }
//    }
//}

class ManualUpdateWidgetCallBack : ActionCallback, KoinComponent {
    private val pref: SharedPreferences by inject(named("widgetPref"))
    private val repository: Repository by inject()
    override suspend fun onRun(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Updating...", Toast.LENGTH_SHORT).show()
        }
        val config = pref.getWidgetConfig(glanceId.widgetId()!!)!!
        Timber.d("GlanceId $glanceId regexId")
        if(config.type == "thai") {
            if(config.location == "all") {
                repository.getTodayFlow().collectLatest {
                    when (it) {
                        is Result.Success -> ThaiAppWidget(today = it.data).update(context, glanceId)
                        else -> Unit
                    }
                }
            } else {
                repository.getTodayByProvince(config.location).collectLatest {
                    when (it) {
                        is Result.Success -> ThaiAppWidget(data = it.data).update(context, glanceId)
                        else -> Unit
                    }
                }
            }

        } else {
            repository.getStatic(config.location).collectLatest {
                when (it) {
                    is Result.Success -> WorldAppWidget(it.data).update(context, glanceId)
                    else -> Unit
                }
            }
        }

    }
}