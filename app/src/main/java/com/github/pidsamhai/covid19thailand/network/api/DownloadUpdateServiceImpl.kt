package com.github.pidsamhai.covid19thailand.network.api

import com.github.pidsamhai.covid19thailand.BuildConfig
import com.github.pidsamhai.covid19thailand.network.Download
import com.github.pidsamhai.covid19thailand.network.ProgressCallBack
import com.github.pidsamhai.covid19thailand.network.ProgressResponseBody
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

class DownloadUpdateServiceImpl : DownloadUpdateService {

    private fun client(
        progressCallBack: ProgressCallBack
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor { chain ->
                val res = chain.proceed(chain.request())

                res.newBuilder()
                    .body(
                        ProgressResponseBody(
                            body = res.body!!,
                            progressCallBack
                        )
                    )
                    .build()
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    override fun dlFile(
        tagName: String,
        fileName: String,
        file: File,
        progressCallBack: ProgressCallBack
    ): Call {
        progressCallBack(Download.Prepare)
        val req = Request.Builder()
            .url("https://github.com/${BuildConfig.OWNER}/${BuildConfig.REPO}/releases/download/${tagName}/${fileName}")
            .build()

        val call = client(progressCallBack).newCall(req)

        call.enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                FileOutputStream(file).use { it.write(response.body?.bytes()) }
                progressCallBack(Download.Finished(file))
                Timber.d(response.body?.contentLength().toString())
            }

            override fun onFailure(call: Call, e: java.io.IOException) {
                if (call.isCanceled()) Timber.d("Cancel")
                else throw IOException("Unexpected code Error")
            }
        })

        return call
    }
}