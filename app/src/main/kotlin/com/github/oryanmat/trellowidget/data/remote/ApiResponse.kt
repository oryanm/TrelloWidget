package com.github.oryanmat.trellowidget.data.remote

sealed interface ApiResponse<out T>

class Success<out T>(val data: T) : ApiResponse<T>

class Error<out T>(val error: String) : ApiResponse<T>