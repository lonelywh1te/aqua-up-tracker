package ru.lonelywh1te.aquaup.domain.usecase

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import ru.lonelywh1te.aquaup.domain.model.convertUnit
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.repository.WaterLogRepository
import ru.lonelywh1te.aquaup.domain.result.AppError
import ru.lonelywh1te.aquaup.domain.result.Result
import ru.lonelywh1te.aquaup.domain.storage.SettingsPreferences
import utils.WaterLogTestFactory
import java.io.IOException
import java.time.LocalDate

class GetWaterLogsByDateUseCaseTest {
    private lateinit var waterLogRepository: WaterLogRepository
    private lateinit var settingsPreferences: SettingsPreferences

    private lateinit var getWaterLogsByDateUseCase: GetWaterLogsByDateUseCase

    private val date = LocalDate.of(2025, 1, 1)

    @BeforeEach
    fun setUp() {
        waterLogRepository = mockk()
        settingsPreferences = mockk()

        getWaterLogsByDateUseCase = GetWaterLogsByDateUseCase(waterLogRepository, settingsPreferences)
    }

    @Test
    fun `return Result_Success with converted logs in Ml sorted by timestamp desc`() = runTest {
        val waterLogs = WaterLogTestFactory.createList(0, 3)

        every { waterLogRepository.getWaterLogsForPeriod(any(), any()) } returns flowOf(Result.Success(waterLogs))
        every { settingsPreferences.volumeUnitFlow } returns flowOf(VolumeUnit.Ml)

        val expected = Result.Success(waterLogs
            .convertUnit(VolumeUnit.Ml, settingsPreferences.volumeUnitFlow.first())
            .sortedByDescending { it.timestamp })

        val actual = getWaterLogsByDateUseCase(date).first()

        assertEquals(expected, actual)
    }

    @Test
    fun `return Result_Success with converted logs in Oz sorted by timestamp desc`() = runTest {
        val waterLogs = WaterLogTestFactory.createList(1, 3)

        every { waterLogRepository.getWaterLogsForPeriod(any(), any()) } returns flowOf(Result.Success(waterLogs))
        every { settingsPreferences.volumeUnitFlow } returns flowOf(VolumeUnit.Oz)

        val expected = Result.Success(waterLogs
            .convertUnit(VolumeUnit.Ml, settingsPreferences.volumeUnitFlow.first())
            .sortedByDescending { it.timestamp })

        val actual = getWaterLogsByDateUseCase(date).first()

        assertEquals(expected, actual)
    }

    @Test
    fun `return Result_Failure with error from repository`() = runTest {
        val exception = IOException()

        every { waterLogRepository.getWaterLogsForPeriod(any(), any()) } returns flowOf(Result.Failure(AppError.Unknown(exception)))
        every { settingsPreferences.volumeUnitFlow } returns flowOf(VolumeUnit.Oz)

        val expected = Result.Failure(AppError.Unknown(exception))
        val actual = getWaterLogsByDateUseCase(date).first()

        assertEquals(expected, actual)
    }

    @Test
    fun `return Result_Success with empty list when repository return empty list`() = runTest {
        val waterLogs = emptyList<WaterLog>()

        every { waterLogRepository.getWaterLogsForPeriod(any(), any()) } returns flowOf(Result.Success(waterLogs))
        every { settingsPreferences.volumeUnitFlow } returns flowOf(VolumeUnit.Oz)

        val expected = Result.Success(waterLogs)
        val actual = getWaterLogsByDateUseCase(date).first()

        assertEquals(expected, actual)
    }
}