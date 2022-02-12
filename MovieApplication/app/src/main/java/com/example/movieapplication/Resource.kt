package com.example.movieapplication


enum class ApiStatus {
    SUCCESS,
    ERROR
}

sealed class Resource<out T>(val status: ApiStatus, val data: T?, val message: String?) {

    data class Success<out R>(val _data: R?) :
        Resource<R>(status = ApiStatus.SUCCESS, data = _data, message = null)
    data class Error(val exception: String) :
        Resource<Nothing>(status = ApiStatus.ERROR, data = null, message = exception)
}