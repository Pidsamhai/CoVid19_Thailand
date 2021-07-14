package com.github.pidsamhai.covid19thailand.repository

import com.github.pidsamhai.covid19thailand.network.ApiResponse
import com.github.pidsamhai.covid19thailand.network.ProgressCallBack
import com.github.pidsamhai.covid19thailand.network.response.github.Release
import okhttp3.Call
import java.io.File

interface GithubRepository {
    suspend fun getRelease(): ApiResponse<Release>
    fun dlFile(
        tagName: String,
        fileName: String,
        file: File,
        progressCallBack: ProgressCallBack
    ): Call
}