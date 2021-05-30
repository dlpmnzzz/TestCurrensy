package com.example.testcurrency.utils

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}

fun <T,R>Result<T>.convert(transformation: (oldResult: Result.Success<T>) -> Result<R>): Result<R> {
    return when (this) {
        is Result.Success -> { transformation(this)}
        is Result.Loading -> Result.Loading
        is Result.Error -> Result.Error(this.exception)
    }
}