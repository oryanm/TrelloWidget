package com.github.oryanmat.trellowidget.util.network

data class DataStatus<out T>(val status: Status, val data: T? = null, val msg: String? = null) {

    enum class Status {
        SUCCESS, ERROR
    }

    companion object {
        fun <T> success(data: T): DataStatus<T> = DataStatus(Status.SUCCESS, data)
        fun <T> error(error: String): DataStatus<T> = DataStatus(Status.ERROR, msg = error)
    }
}