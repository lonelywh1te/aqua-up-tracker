package ru.lonelywh1te.aquaup.domain.result

import ru.lonelywh1te.aquaup.domain.result.Result

sealed class Result<out T> {
    data class Success<out T>(val data: T): Result<T>()
    data object Loading: Result<Nothing>()
    data class Failure(val error: BaseError): Result<Nothing>() // TODO: Custom Errors?
}

fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> {
    return when(this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Loading -> Result.Loading
        is Result.Failure -> Result.Failure(error)
    }
}

fun <A, B, R> combineResult(r1: Result<A>, r2: Result<B>, transform: (A, B) -> R): Result<R> {
    return when {
        r1 is Result.Failure -> Result.Failure(r1.error)
        r2 is Result.Failure -> Result.Failure(r2.error)
        r1 is Result.Loading || r2 is Result.Loading -> Result.Loading
        r1 is Result.Success && r2 is Result.Success -> Result.Success(transform(r1.data, r2.data))
        else -> throw IllegalStateException("Unexpected result types")
    }
}

inline fun <T, R> Result<T>.handle(
    onSuccess: (T) -> R,
    onFailure: (BaseError) -> R,
    onLoading: () -> R
): R = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Failure -> onFailure(error)
    is Result.Loading -> onLoading()
}


