package com.github.pidsamhai.covid19thailand.db

sealed class Result<out T: Any> {
    object Initial: Result<Nothing>()
    object Loading: Result<Nothing>()
    object Fail: Result<Nothing>()
    data class Success<out T: Any>(val data: T): Result<T>()
}