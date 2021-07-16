package com.github.pidsamhai.covid19thailand.network.api

import com.github.pidsamhai.covid19thailand.BuildConfig
import com.github.pidsamhai.covid19thailand.network.response.ddc.TimeLine
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://covid19.th-stat.com/json/covid19v2/"

interface Covid19ApiServices {
    @GET("getTodayCases.json")
    suspend fun getToDay(): Today

    @GET("getTimeline.json")
    suspend fun getTimeline(): TimeLine

    companion object {
        fun create(): Covid19ApiServices {
            val okHttpClient = OkHttpClient.Builder().apply {
                if (BuildConfig.DEBUG) {
                    addNetworkInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }.build()


            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Covid19ApiServices::class.java)
        }

    }
}