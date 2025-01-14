package com.amirx.matule_app_sessions.data.datasource.network

import co.touchlab.kermit.Message

sealed class ResponseState<T> {
    class Success<T>(val data: T) : ResponseState<T>()
    class Error<T>(val message: String) : ResponseState<T>()
    class Loading<T>() : ResponseState<T>()

}