package ru.lonelywh1te.aquaup.domain.model.settings

import ru.lonelywh1te.aquaup.domain.model.settings.ReminderInterval.Custom
import ru.lonelywh1te.aquaup.domain.model.settings.ReminderInterval.None
import ru.lonelywh1te.aquaup.domain.model.settings.ReminderInterval.OneHour
import ru.lonelywh1te.aquaup.domain.model.settings.ReminderInterval.ThreeHours
import ru.lonelywh1te.aquaup.domain.model.settings.ReminderInterval.TwoHours
import java.time.LocalTime

sealed class ReminderInterval {
    data object None: ReminderInterval()

    data object OneHour: ReminderInterval()

    data object TwoHours: ReminderInterval()

    data object ThreeHours: ReminderInterval()

    data class Custom(val times: List<LocalTime>): ReminderInterval()

    companion object {
        fun getAll(): List<ReminderInterval> {
            return listOf(OneHour, TwoHours, ThreeHours, Custom(emptyList()))
        }

        fun fromName(name: String, times: List<LocalTime> = emptyList()): ReminderInterval {
            return when(name) {
                "one_hour" -> OneHour
                "two_hours" -> TwoHours
                "three_hours" -> ThreeHours
                "custom" -> Custom(times)
                else -> None
            }
        }
    }
}

fun ReminderInterval.name(): String {
    return when(this) {
        is None -> "none"
        is OneHour -> "one_hour"
        is TwoHours -> "two_hours"
        is ThreeHours -> "three_hours"
        is Custom -> "custom"
    }
}