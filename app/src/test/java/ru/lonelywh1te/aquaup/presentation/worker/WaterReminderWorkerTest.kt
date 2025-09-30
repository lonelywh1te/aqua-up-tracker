package ru.lonelywh1te.aquaup.presentation.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.lonelywh1te.aquaup.presentation.notification.AppNotificationManager

class WaterReminderWorkerTest {
    private lateinit var context: Context
    private lateinit var workerParameters: WorkerParameters
    private lateinit var notificationManager: AppNotificationManager

    private lateinit var waterReminderWorker: WaterReminderWorker

    @BeforeEach
    fun setUp() {
        context = mockk()
        workerParameters = mockk()
        notificationManager = mockk(relaxed = true)

        waterReminderWorker = WaterReminderWorker(context, workerParameters, notificationManager)
    }

    @Test
    fun `worker show notification and return success`() {
        val result = waterReminderWorker.doWork()

        verify(exactly = 1) { notificationManager.showWaterReminderNotification() }
        assertEquals(ListenableWorker.Result.success(), result)
    }
}