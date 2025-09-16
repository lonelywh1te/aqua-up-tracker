package ru.lonelywh1te.aquaup.data.error

import androidx.sqlite.SQLiteException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import ru.lonelywh1te.aquaup.domain.result.AppError
import ru.lonelywh1te.aquaup.domain.result.DatabaseError
import ru.lonelywh1te.aquaup.domain.result.Result
import timber.log.Timber

suspend fun <T> dbRunCatchingWithResult(action: suspend () -> T): Result<T> {
    return try {
        Result.Success(action())
    } catch (e: Exception) {
        Timber.e(e)

        when (e) {
            is SQLiteException -> Result.Failure(DatabaseError.SQLite(e))
            else -> Result.Failure(AppError.Unknown(e))
        }
    }
}

fun <T> Flow<Result<T>>.asDbSafeFlow(): Flow<Result<T>> {
    return this.catch { e ->
        Timber.e(e)

        val result = when (e) {
            is SQLiteException -> Result.Failure(DatabaseError.SQLite(e))
            else -> Result.Failure(AppError.Unknown(e))
        }

        emit(result)
    }
}