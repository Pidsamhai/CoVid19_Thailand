package com.github.pidsamhai.covid19thailand.network

import java.io.File

sealed class Download {
    object Prepare: Download()
    data class Progress(val percent: Float) : Download()
    data class Finished(val file: File) : Download()
}