package com.github.pidsamhai.covid19thailand.network

import com.github.pidsamhai.covid19thailand.db.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import timber.log.Timber

// ResultType: Type for the Resource data.
// RequestType: Type for the API response.

inline fun <ResultType: Any, RequestType: Any> networkBoundResource(
    crossinline loadFromDb: suspend () -> ResultType?,
    crossinline createCall: suspend () -> ApiResponse<RequestType>,
    crossinline saveCallResult: suspend (RequestType) -> Unit,
    crossinline onFetchFailed: (Throwable) -> Unit = { },
    crossinline shouldFetch: (ResultType?) -> Boolean = { true }
) = flow {
    /**
     * Fist emit data if has cache
     */
    val dbValue = loadFromDb()

    if (dbValue != null) emit(Result.Success(dbValue))

    delay(60)

    /**
     * Feed new Data
     */
    when {
        shouldFetch(dbValue) -> {
            when(val res = createCall()) {
                is ApiResponse.Success -> {
                    /**
                     * Emit Loading
                     */
                    emit(Result.Loading)

                    delay(60)
                    saveCallResult(res.data)
                    val data = loadFromDb()
                    Timber.d("Data %s", data.toString())
                    if (data != null) {
                        emit(Result.Success(data))
                    } else {
                        emit(Result.Fail(Exception("Data is Empty")))
                    }
                }

                is ApiResponse.Error -> emit(Result.Fail(res.data))
            }
        }
    }
}

inline fun <ResultType: Any, RequestType : Any> networkBoundResource(
    crossinline query: () -> Flow<ResultType?>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline onFetchFailed: (Exception) -> Unit = {  },
    crossinline shouldFetch: (ResultType?) -> Boolean = { true }
) = flow {
    val data = query()

    val flow = if (shouldFetch(data.first())) {
        emit(Result.Loading)

        try {
            saveFetchResult(fetch())
            query().map { it?.let{ Result.Success(it) } ?: Result.Fail(Exception("Data is Empty")) }
        } catch (e: Exception) {
            Timber.e(e)
            onFetchFailed(e)
            query().map { Result.Fail(e) }
        }
    } else {
        data.map { it?.let{ Result.Success(it)} ?: Result.Fail(Exception("Data is Empty")) }
    }

    emitAll(flow)
}.flowOn(Dispatchers.IO)