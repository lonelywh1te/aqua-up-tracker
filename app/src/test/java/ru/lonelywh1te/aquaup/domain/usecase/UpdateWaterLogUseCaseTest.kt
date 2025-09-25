package ru.lonelywh1te.aquaup.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.repository.WaterLogRepository
import ru.lonelywh1te.aquaup.domain.storage.SettingsPreferences

class UpdateWaterLogUseCaseTest {

    private lateinit var waterLogRepository: WaterLogRepository
    private lateinit var settingsPreferences: SettingsPreferences

    private lateinit var updateWaterLogUseCase: UpdateWaterLogUseCase

    @BeforeEach
    fun setUp() {
        waterLogRepository = mockk(relaxed = true)
        settingsPreferences = mockk()

        updateWaterLogUseCase = UpdateWaterLogUseCase(waterLogRepository, settingsPreferences)
    }

    @Test
    fun `update water log in ml without conversion`() = runTest {
        coEvery { settingsPreferences.volumeUnitFlow } returns flowOf(VolumeUnit.Ml)

        updateWaterLogUseCase(WaterLog(amountMl = 200))

        coVerify { waterLogRepository.updateWaterLog(match { it.amountMl == 200 }) }
    }

    @Test
    fun `converts oz to ml before update`() = runTest {
        coEvery { settingsPreferences.volumeUnitFlow } returns flowOf(VolumeUnit.Oz)

        updateWaterLogUseCase(WaterLog(amountMl = 1))

        coVerify { waterLogRepository.updateWaterLog(match { it.amountMl == 30 }) }
    }

    @Test
    fun `update water log only once`() = runTest {
        coEvery { settingsPreferences.volumeUnitFlow } returns flowOf(VolumeUnit.Ml)

        updateWaterLogUseCase(WaterLog())

        coVerify(exactly = 1) { waterLogRepository.updateWaterLog(any()) }
    }
}