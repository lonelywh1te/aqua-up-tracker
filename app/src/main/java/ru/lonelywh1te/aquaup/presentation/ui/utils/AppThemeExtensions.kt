package ru.lonelywh1te.aquaup.presentation.ui.utils

import androidx.annotation.StringRes
import ru.lonelywh1te.aquaup.R
import ru.lonelywh1te.aquaup.domain.model.settings.AppTheme

@StringRes
fun AppTheme.stringRes(): Int {
    return when (this) {
        AppTheme.Light -> R.string.light
        AppTheme.Dark -> R.string.dark
        AppTheme.System -> R.string.as_system
    }
}