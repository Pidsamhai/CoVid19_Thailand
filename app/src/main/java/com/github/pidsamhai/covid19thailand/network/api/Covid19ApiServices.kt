package com.github.pidsamhai.covid19thailand.network.api

import androidx.lifecycle.LiveData
import com.github.pidsamhai.covid19thailand.network.response.TimeLine
import com.github.pidsamhai.covid19thailand.network.response.Today
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://covid19.th-stat.com/api/open/"

interface Covid19ApiServices {
    @GET("today")
    fun getToDay() : LiveData<Today>

    @GET("today")
    fun getToDayNomal() : Call<Today>?

    @GET("timeline")
    fun getTimeline() : Call<TimeLine>?

    companion object {
        operator fun invoke(
            //connectivityInterceptor: ConnectivityInterceptor
        ): Covid19ApiServices {
            val requestInterceptor = Interceptor { chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
//                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Covid19ApiServices::class.java)
        }

    }
}