package com.github.pidsamhai.covid19thailand.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.github.pidsamhai.covid19thailand.worker.WidgetUpdateWorker
import java.util.concurrent.TimeUnit

class CheckWorkerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_USER_PRESENT && context != null) {
            val workManager = WorkManager.getInstance(context)
            val works = workManager.getWorkInfosForUniqueWork(WidgetUpdateWorker.NAME).get()
            if (works.isEmpty()) {
                val task = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(1, TimeUnit.HOURS)
                    .build()
                workManager.enqueueUniquePeriodicWork(
                    WidgetUpdateWorker.NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    task
                )
            }
        }
    }
}