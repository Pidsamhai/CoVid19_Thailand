package com.github.pidsamhai.covid19thailand.network.api

import com.github.pidsamhai.covid19thailand.network.response.rapid.country.Countries
import com.github.pidsamhai.covid19thailand.network.response.rapid.fuckyou.Static
import com.github.pidsamhai.covid19thailand.network.response.rapid.hell.Statics
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://covid-193.p.rapidapi.com/"
private const val apiKey = "" // Your Api Key
private const val apiHost = "covid-193.p.rapidapi.com"

interface CoVid19RapidApiServices {
//    @GET("history")
//    fun getHistory(): Call<Any>
//
    @GET("countries")
    suspend fun getContiresName(): Countries

    @GET("statistics")
    suspend fun getStatics(): Statics

    @GET("statistics")
    suspend fun getStatic(@Query("country")country:String) : Static


    companion object {
        operator fun invoke(): CoVid19RapidApiServices {
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
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CoVid19RapidApiServices::class.java)
        }
    }
}