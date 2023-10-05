package com.github.oryanmat.trellowidget.data.remote


sealed class ApiResponse<out T> {

    data class Success<out T>(val data: T) : ApiResponse<T>()

    data class Error<out T>(val error: String) : ApiResponse<T>()
}