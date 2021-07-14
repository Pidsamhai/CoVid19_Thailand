package com.github.pidsamhai.covid19thailand.repository

import com.github.pidsamhai.covid19thailand.BuildConfig
import com.github.pidsamhai.covid19thailand.network.ApiResponse
import com.github.pidsamhai.covid19thailand.network.ProgressCallBack
import com.github.pidsamhai.covid19thailand.network.api.DownloadUpdateService
import com.github.pidsamhai.covid19thailand.network.api.GithubApiService
import com.github.pidsamhai.covid19thailand.network.response.github.Release
import okhttp3.Call
import timber.log.Timber
import java.io.File

class GithubRepositoryImpl(
    private val githubApiService: GithubApiService,
    private val downloadUpdateService: DownloadUpdateService
) : GithubRepository {
    override suspend fun getRelease(): ApiResponse<Release> {
        return try {
            val result = githubApiService.getRelease(owner = BuildConfig.OWNER, repo = BuildConfig.REPO)
            Timber.d(result.toString())
            ApiResponse.Success(result)
        } catch (e: Exception) {
            Timber.e(e)
            ApiResponse.Error(e)
        }
    }

    override fun dlFile(
        tagName: String,
        fileName: String,
        file: File,
        progressCallBack: ProgressCallBack
    ): Call = downloadUpdateService.dlFile(
        tagName,
        fileName,
        file,
        progressCallBack
    )
}