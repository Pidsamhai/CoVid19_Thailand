package com.github.pidsamhai.covid19thailand.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import java.io.File

sealed class Download {
    object Prepare: Download()
    data class Progress(val percent: Float) : Download()
    data class Finished(val file: File) : Download()
}