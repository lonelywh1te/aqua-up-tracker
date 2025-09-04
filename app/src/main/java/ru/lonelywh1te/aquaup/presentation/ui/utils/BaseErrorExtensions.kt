package ru.lonelywh1te.aquaup.presentation.ui.utils

import androidx.annotation.StringRes
import ru.lonelywh1te.aquaup.R
import ru.lonelywh1te.aquaup.domain.result.AppError
import ru.lonelywh1te.aquaup.domain.result.BaseError
import ru.lonelywh1te.aquaup.domain.result.DatabaseError

@StringRes
fun BaseError.asStringRes(): Int {
    return when (this) {
        is AppError -> this.asStringRes()
        is DatabaseError -> this.asStringRes()
    }
}

@StringRes
fun AppError.asStringRes(): Int {
    return when(this) {
        is AppError.Unknown -> R.string.app_error_unknown_message
    }
}

@StringRes
fun DatabaseError.asStringRes(): Int {
    return when(this) {
        is DatabaseError.SQLite -> R.string.database_error_sqlite_message
    }
}
