package ru.lonelywh1te.aquaup.domain.model

import ru.lonelywh1te.aquaup.domain.model.settings.ReminderInterval
import java.time.LocalTime

data class ReminderSchedule(
    val interval: ReminderInterval,
    val times: List<LocalTime> = emptyList()
)
