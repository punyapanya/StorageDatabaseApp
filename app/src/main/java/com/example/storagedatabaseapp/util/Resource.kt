package com.example.storagedatabaseapp.util

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T) = Resource(Status.SUCCESS, data, null)
        fun <T> error(message: String, data: T? = null) = Resource(Status.ERROR, data, message)
        fun <T> loading(data: T? = null) = Resource(Status.LOADING, data, null)
    }
}