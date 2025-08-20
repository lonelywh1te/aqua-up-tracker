package ru.lonelywh1te.aquaup.domain.model.settings

import kotlin.math.roundToInt

enum class VolumeUnit {
    Ml, Oz;

}

fun convertMlToOz(ml: Int): Int {
    return (ml / 29.5735).roundToInt()
}

fun convertOzToMl(oz: Int): Int {
    return (oz * 29.5735).roundToInt()
}