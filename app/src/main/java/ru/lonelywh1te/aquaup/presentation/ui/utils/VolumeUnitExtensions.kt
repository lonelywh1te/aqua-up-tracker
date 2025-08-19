package ru.lonelywh1te.aquaup.presentation.ui.utils

import androidx.annotation.StringRes
import ru.lonelywh1te.aquaup.R
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit

@StringRes
fun VolumeUnit.valueStringRes(): Int {
    return when(this) {
        VolumeUnit.Ml -> R.string.ml
        VolumeUnit.Oz -> R.string.oz
    }
}