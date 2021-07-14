package com.github.pidsamhai.covid19thailand.network.api

import com.github.pidsamhai.covid19thailand.network.response.github.Release
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://api.github.com/"

interface GithubApiService {


    @GET("/repos/{owner}/{repo}/releases")
    suspend fun getRelease(@Path("owner") owner: String, @Path("repo") repo: String): Release

    companion object {
        fun create(): GithubApiService {
            val requestInterceptor = Interceptor { chain ->

                val url = chain.request().url.newBuilder()
                    .addQueryParameter("per_page", "1")
                    .addQueryParameter("page", "1")
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .header("accept", "application/vnd.github.v3+json")
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addNetworkInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubApiService::class.java)
        }
    }
}