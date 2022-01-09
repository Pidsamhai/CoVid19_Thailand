package com.github.pidsamhai.covid19thailand.worker

import android.content.Context
import android.content.SharedPreferences
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.pidsamhai.covid19thailand.appwidget.ThaiAppWidget
import com.github.pidsamhai.covid19thailand.appwidget.WorldAppWidget
import com.github.pidsamhai.covid19thailand.repository.Repository
import com.github.pidsamhai.covid19thailand.utilities.getWidgetConfig
import com.github.pidsamhai.covid19thailand.utilities.widgetId
import kotlinx.coroutines.flow.lastOrNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import timber.log.Timber
import com.github.pidsamhai.covid19thailand.db.Result as ResponseResult

class WidgetUpdateWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {
    private val pref: SharedPreferences by inject(named("widgetPref"))
    private val repository: Repository by inject()

    private val glanceAppwidgetManager = GlanceAppWidgetManager(context)
    override suspend fun doWork(): Result {
        val glanceIds = listOf(
            glanceAppwidgetManager.getGlanceIds(ThaiAppWidget::class.java),
            glanceAppwidgetManager.getGlanceIds(WorldAppWidget::class.java)
        ).flatten()

        return try {
            if (glanceIds.isEmpty()) {
                Timber.d("No Widget Left")
            }

            for (glanceId in glanceIds) {
                Timber.d("Read config ${glanceId.widgetId()}")
                val config = pref.getWidgetConfig(glanceId.widgetId()!!)!!
                Timber.d("Update config $config")
                if (config.type == "thai") {
                    if(config.location == "all") {
                        val result = repository.getTodayFlow().lastOrNull()
                        if (result is ResponseResult.Success) {
                            ThaiAppWidget(today = result.data).update(context, glanceId)
                        }
                    } else {
                        val result = repository.getTodayByProvince(config.location, true).lastOrNull()
                        if (result is ResponseResult.Success) {
                            ThaiAppWidget(result.data).update(context, glanceId)
                        }
                    }
                } else {
                    val result = repository.getStatic(config.location).lastOrNull()
                    if (result is ResponseResult.Success) {
                        WorldAppWidget(result.data).update(context, glanceId)
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure()
        }
    }

    companion object {
        const val NAME = "UPDATE_WORLD_WIDGET"
    }
}