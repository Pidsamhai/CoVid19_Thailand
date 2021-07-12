package com.github.pidsamhai.covid19thailand.db

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.datetime.*
import timber.log.Timber

class LastFetchImpl(
    private val pref: SharedPreferences
) : LastFetch {
    override val shouldFetchToday: Boolean
        get() = calculateExpTime(ShouldFetchTodayKey) >= EXP_MINUTE
    override val shouldFetchTimeLine: Boolean
        get() = calculateExpTime(ShouldFetchTimeLineKey) >= EXP_MINUTE
    override val shouldFetchCountry: Boolean
        get() = calculateExpTime(ShouldFetchCountryKey) >= EXP_MINUTE

    override fun saveLastFetch(key: String) {
        val now = Clock.System.now().toEpochMilliseconds()
        Timber.d("Save Last Fetch key -> $key val -> $now")
        pref.edit { putLong(key, now) }
    }

    private fun calculateExpTime(key: String): Long {
        val last = pref.getLong(key, -1L)

        if (last == -1L) {
            Timber.d("INIT FETCH key -> $key")
            saveLastFetch(key)
            return last
        }
        val period = Instant.fromEpochMilliseconds(last)
            .until(Clock.System.now(), DateTimeUnit.MINUTE, TimeZone.currentSystemDefault())
        Timber.d("Time Diff key -> $key $period")
        return period
    }

    override fun saveLastFetchToday() = saveLastFetch(ShouldFetchTodayKey)

    override fun saveLastFetchTimeline() = saveLastFetch(ShouldFetchTimeLineKey)

    override fun saveLastFetchCountry() = saveLastFetch(ShouldFetchCountryKey)

    companion object {
        private const val EXP_MINUTE = 10L
        private const val ShouldFetchTodayKey = "ShouldFetchTodayKey"
        private const val ShouldFetchTimeLineKey = "ShouldFetchTimeLine"
        private const val ShouldFetchCountryKey = "ShouldFetchCountry"
    }
}