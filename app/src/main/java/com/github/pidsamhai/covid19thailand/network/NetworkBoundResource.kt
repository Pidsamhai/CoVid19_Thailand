package com.github.pidsamhai.covid19thailand.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay
import com.github.pidsamhai.covid19thailand.db.Result

// ResultType: Type for the Resource data.
// RequestType: Type for the API response.
abstract class NetworkBoundResource<ResultType: Any, RequestType: Any> {

    fun asFlow(): Flow<Result<ResultType>> = flow {
        /**
         * Fist emit data if has cache
         */
        val dbValue = loadFromDb()

        if (dbValue != null) emit(Result.Success(dbValue))

        delay(60)

        /**
         * Emit Loading
         */
        emit(Result.Loading)

        delay(60)

        /**
         * Feed new Data
         */
        when {
            shouldFetch(dbValue) -> {
                when(val res = createCall()) {
                    is ApiResponse.Success -> {
                        saveCallResult(res.data)
                        emit(Result.Success(loadFromDb()!!))
                    }

                    is ApiResponse.Error -> emit(Result.Fail)
                }
            }
        }
    }

    fun asLiveData(): LiveData<Result<ResultType>> = asFlow().asLiveData()

    // Called to save the result of the API response into the database
    protected abstract suspend fun saveCallResult(item: RequestType)

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    // Called to get the cached data from the database.
    protected abstract suspend fun loadFromDb(): ResultType?

    // Called to create the API call.
    protected abstract suspend fun createCall(): ApiResponse<RequestType>

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    protected open fun onFetchFailed() {}
}

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
                    emit(Result.Success(loadFromDb()!!))
                }

                is ApiResponse.Error -> emit(Result.Fail)
            }
        }
    }
}