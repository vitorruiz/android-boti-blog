package br.com.vitorruiz.botiblog.core

sealed class ResultWrapper<T> {
    class Loading<T> : ResultWrapper<T>()
    class Success<T>(val data: T) : ResultWrapper<T>()
    class Error<T>(
        val message: String,
        val data: T? = null,
        val isConnectionError: Boolean = false
    ) : ResultWrapper<T>()
}