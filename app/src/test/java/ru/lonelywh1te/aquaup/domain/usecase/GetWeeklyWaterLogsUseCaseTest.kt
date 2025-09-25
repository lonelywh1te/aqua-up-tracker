package ru.lonelywh1te.aquaup.domain.usecase

import android.R.attr.data
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.repository.WaterLogRepository
import ru.lonelywh1te.aquaup.domain.result.AppError
import ru.lonelywh1te.aquaup.domain.result.Result
import ru.lonelywh1te.aquaup.domain.storage.SettingsPreferences
import utils.WaterLogTestFactory
import java.io.IOException
import java.time.LocalDate

class GetWeeklyWaterLogsUseCaseTest {
    private lateinit var waterLogRepository: WaterLogRepository
    private lateinit var settingsPreferences: SettingsPreferences

    private lateinit var getWeeklyWaterLogsUseCase: GetWeeklyWaterLogsUseCase

    @BeforeEach
    fun setUp() {
        waterLogRepository = mockk()
        settingsPreferences = mockk()

        getWeeklyWaterLogsUseCase = GetWeeklyWaterLogsUseCase(waterLogRepository, settingsPreferences)
    }

    @Test
    fun `return grouped weekly logs with empty days`() = runTest {
        val weekStart = LocalDate.of(2025, 9, 22)
        val waterLogs = listOf(
            WaterLogTestFactory.create(1, timestamp = weekStart.atStartOfDay().plusHours(10)),
            WaterLogTestFactory.create(2, timestamp = weekStart.atStartOfDay().plusDays(2).plusHours(15))
        )

        every { waterLogRepository.getWaterLogsForPeriod(any(), any()) } returns flowOf(Result.Success(waterLogs))
        every { settingsPreferences.volumeUnitFlow } returns flowOf(VolumeUnit.Ml)

        val result = getWeeklyWaterLogsUseCase(weekStart).first()
        val data = (result as Result.Success).data

        assertEquals(7, data.size)
    }

    @Test
    fun `convert logs when unit is Oz`() = runTest {
        val weekStart = LocalDate.of(2025, 9, 22)
        val logs = listOf(
            WaterLogTestFactory.create(1, amountMl = 30, timestamp = weekStart.atStartOfDay())
        )

        every { waterLogRepository.getWaterLogsForPeriod(any(), any()) } returns flowOf(Result.Success(logs))
        every { settingsPreferences.volumeUnitFlow } returns flowOf(VolumeUnit.Oz)

        val result = getWeeklyWaterLogsUseCase(weekStart).first()
        val data = (result as Result.Success).data

        val expected = 1
        val actual = data[weekStart]!!.first().amountMl

        assertEquals(expected, actual)
    }

    @Test
    fun `propagate failure from repository`() = runTest {
        val weekStart = LocalDate.of(2025, 9, 22)
        val exception = IOException()

        every { waterLogRepository.getWaterLogsForPeriod(any(), any()) } returns flowOf(Result.Failure(AppError.Unknown(exception)))
        every { settingsPreferences.volumeUnitFlow } returns flowOf(VolumeUnit.Ml)

        val expected = Result.Failure(AppError.Unknown(exception))
        val actual = getWeeklyWaterLogsUseCase(weekStart).first()

        assertEquals(expected, actual)
    }

    @Test
    fun `return empty lists when repository returns empty`() = runTest {
        val weekStart = LocalDate.of(2025, 9, 22)

        every { waterLogRepository.getWaterLogsForPeriod(any(), any()) } returns flowOf(Result.Success(emptyList()))
        every { settingsPreferences.volumeUnitFlow } returns flowOf(VolumeUnit.Ml)

        val result = getWeeklyWaterLogsUseCase(weekStart).first()
        val data = (result as Result.Success).data

        assertEquals(7, data.size)
        assertTrue(data.values.all { it.isEmpty() })
    }
}