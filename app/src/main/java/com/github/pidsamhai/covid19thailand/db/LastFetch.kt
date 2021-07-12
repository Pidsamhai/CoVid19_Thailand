package com.github.pidsamhai.covid19thailand.db

interface LastFetch {
    val shouldFetchToday: Boolean
    val shouldFetchTimeLine: Boolean
    val shouldFetchCountry: Boolean

    fun saveLastFetch(key: String)
    fun saveLastFetchToday()
    fun saveLastFetchTimeline()
    fun saveLastFetchCountry()
}