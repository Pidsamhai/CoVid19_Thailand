package com.github.pidsamhai.covid19thailand.network

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

typealias ProgressCallBack = (
    download: Download
) -> Unit

class ProgressResponseBody(
    val body: ResponseBody,
    val callBack: ProgressCallBack,
) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentLength(): Long = body.contentLength()

    override fun contentType(): MediaType? = body.contentType()

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = ss(body.source()).buffer()
        }

        return bufferedSource!!
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun ss(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0L
                if (bytesRead != -1L) {
                    callBack(Download.Progress(totalBytesRead.toFloat() / body.contentLength()))
                }
                Timber.d("BytesRead: $totalBytesRead Finished: ${bytesRead == -1L}")
                return bytesRead
            }
        }
    }
}