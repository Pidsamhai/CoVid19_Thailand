package com.github.pidsamhai.covid19thailand.db

sealed class Result<out T: Any> {
    object Initial: Result<Nothing>()
    object Loading: Result<Nothing>()
    data class Fail(val exception: Exception): Result<Nothing>()
    data class Success<out T: Any>(val data: T): Result<T>()
}