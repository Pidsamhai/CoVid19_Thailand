package com.github.pidsamhai.covid19thailand.db

import com.github.pidsamhai.covid19thailand.network.response.github.ReleaseItem

interface LastFetch {
    val shouldFetchToday: Boolean
    val shouldFetchTimeLine: Boolean
    val shouldFetchCountry: Boolean
    val shouldFetchTodayByProvince: Boolean

    fun saveLastFetch(key: String)
    fun saveLastFetchToday()
    fun saveLastFetchTodayByProvince()
    fun saveLastFetchTimeline()
    fun saveLastFetchCountry()

    fun saveReleaseItem(releaseItem: ReleaseItem)

    fun getReleaseItem(): ReleaseItem?

    fun removeReleaseItem()
}