package ru.lonelywh1te.aquaup.presentation.reminder

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ru.lonelywh1te.aquaup.domain.model.ReminderSchedule
import ru.lonelywh1te.aquaup.domain.model.settings.ReminderInterval
import ru.lonelywh1te.aquaup.presentation.ui.utils.getLocalTimesTo
import ru.lonelywh1te.aquaup.presentation.worker.WaterReminderWorker
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class WaterReminder(
    private val context: Context,
) {
    private val workManager = WorkManager.getInstance(context)
    private val minTimeToRemind = LocalTime.of(8, 0)
    private val maxTimeToRemind = LocalTime.of(22, 0)

    fun setSchedule(schedule: ReminderSchedule) {
        clearAllWork()

        val timesToWork = when (schedule.interval) {
            ReminderInterval.None -> emptyList()
            ReminderInterval.EveryOneHour -> minTimeToRemind.getLocalTimesTo(maxTimeToRemind, 1)
            ReminderInterval.EveryTwoHours -> minTimeToRemind.getLocalTimesTo(maxTimeToRemind, 2)
            ReminderInterval.EveryThreeHours -> minTimeToRemind.getLocalTimesTo(maxTimeToRemind, 3)
            ReminderInterval.Custom -> schedule.times
        }

        enqueuePeriodicWork(timesToWork)
    }

    private fun enqueuePeriodicWork(times: List<LocalTime>) {
        if (times.isEmpty()) return

        val now = LocalDateTime.now()

        times.forEach { time ->
            val initialTime = now.withHour(time.hour).withMinute(time.minute)
            val workTime = if (initialTime.isBefore(now)) initialTime.plusDays(1) else initialTime

            val initialDelay = Duration.between(now, workTime).toMillis()

            val request = PeriodicWorkRequestBuilder<WaterReminderWorker>(Duration.ofHours(24))
                .addTag(WORK_TAG)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .build()

            workManager.enqueueUniquePeriodicWork(
                uniqueWorkName = "water_reminder_${time.hour}_${time.minute}",
                existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.REPLACE,
                request
            )
        }
    }

    private fun clearAllWork() {
        workManager.cancelAllWorkByTag(WORK_TAG)
    }

    companion object {
        private const val WORK_TAG = "water_reminder"
    }
}