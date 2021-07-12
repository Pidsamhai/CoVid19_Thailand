package com.github.pidsamhai.covid19thailand.network

sealed class ApiResponse<out T: Any> {
    data class Success<out T: Any>(val data: T): ApiResponse<T>()
    data class Error(val data: Exception): ApiResponse<Nothing>()
}