package com.example.newsapp.util

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

enum class ErrorCodes(val code: Int) {
    SocketConnectionTimeout(-1),
    NoInternetConnection(-2)
}

@Singleton
class ResponseHandler @Inject constructor() {

    fun <T> handleSuccess(data: T): Resource<T> {
        return Resource.Success(data)
    }

    fun <T> handleException(e: Exception, data: T? = null): Resource<T> {
        e.printStackTrace()
        return when (e) {
            is HttpException -> Resource.Error(getErrorMessage(e.code()), data)
            is IOException -> Resource.Error(
                getErrorMessage(ErrorCodes.NoInternetConnection.code),
                data
            )
            is SocketTimeoutException -> Resource.Error(
                getErrorMessage(ErrorCodes.SocketConnectionTimeout.code),
                data
            )
            else -> Resource.Error(getErrorMessage(Int.MIN_VALUE), data)
        }

    }

    private fun getErrorMessage(code: Int): String {
        return when (code) {
            ErrorCodes.NoInternetConnection.code -> "Нет соединения с интернетом"
            ErrorCodes.SocketConnectionTimeout.code -> "Время ожидания истекло"
            401 -> "Войдите в аккаунт"
            403 -> "Запрещено"
            404 -> "Не найдено"
            else -> "Неизвестная ошибка"
        }
    }
}