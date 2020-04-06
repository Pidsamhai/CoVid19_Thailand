package com.github.pidsamhai.covid19thailand

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import java.util.concurrent.Executor
import java.util.concurrent.Executors

open class AppExecutors (
    private val diskIO:Executor,
    private val networkIO:Executor,
    private val mainThead:Executor
){
    constructor() : this(
        Executors.newSingleThreadExecutor(),
        Executors.newFixedThreadPool(3),
        MainThreadExecutor()
    )


    fun diskIO(): Executor {
        return diskIO
    }

    fun networkIO(): Executor {
        return networkIO
    }

    fun mainThread(): Executor {
        return mainThead
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}