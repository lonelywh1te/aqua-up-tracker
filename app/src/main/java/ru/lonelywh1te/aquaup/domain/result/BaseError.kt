package ru.lonelywh1te.aquaup.domain.result

sealed interface BaseError {
    val e: Throwable
}

sealed class AppError(override val e: Throwable): BaseError {
    data class Unknown(override val e: Throwable): AppError(e)
}

sealed class DatabaseError(override val e: Exception): BaseError {
    data class SQLite(override val e: Exception): DatabaseError(e)
}

