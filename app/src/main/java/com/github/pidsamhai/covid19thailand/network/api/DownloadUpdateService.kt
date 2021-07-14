package com.github.pidsamhai.covid19thailand.network.api

import com.github.pidsamhai.covid19thailand.network.ProgressCallBack
import okhttp3.Call
import java.io.File

interface DownloadUpdateService {
    fun dlFile(
        tagName: String,
        fileName: String,
        file: File,
        progressCallBack: ProgressCallBack
    ): Call
}