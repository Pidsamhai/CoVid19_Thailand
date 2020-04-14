package com.github.pidsamhai.covid19thailand.network.api

import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Statics
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.history.HistoryResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://covid-193.p.rapidapi.com/"
private const val apiKey = "" // Your Api Key https://rapidapi.com/
private const val apiHost = "covid-193.p.rapidapi.com"

interface CoVid19RapidApiServices {

    @GET("history")
    suspend fun getHistories(@Query("country") country: String): HistoryResponse

    @GET("statistics")
    suspend fun getStatics(): Statics

    @GET("statistics")
    suspend fun getStatic(@Query("country") country: String): Static


    companion object {
        fun create(): CoVid19RapidApiServices {
            val requestInterceptor = Interceptor { chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .header("x-rapidapi-host", apiHost)
                    .header("x-rapidapi-key", apiKey)
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CoVid19RapidApiServices::class.java)
        }
    }
}