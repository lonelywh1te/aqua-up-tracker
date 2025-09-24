package ru.lonelywh1te.aquaup.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.repository.WaterLogRepository
import ru.lonelywh1te.aquaup.domain.storage.SettingsPreferences

class AddWaterUseCaseTest {
    private lateinit var waterLogRepository: WaterLogRepository
    private lateinit var settingsPreferences: SettingsPreferences

    private lateinit var addWaterUseCase: AddWaterUseCase

    @BeforeEach
    fun setUp() {
        waterLogRepository = mockk(relaxed = true)
        settingsPreferences = mockk()

        addWaterUseCase = AddWaterUseCase(waterLogRepository, settingsPreferences)
    }

    @Test
    fun `do nothing when amount is 0`() = runTest {
        addWaterUseCase(0)

        coVerify(exactly = 0) { waterLogRepository.addWaterLog(any()) }
    }

    @Test
    fun `adds water log in ml without conversion`() = runTest {
        coEvery { settingsPreferences.volumeUnitFlow } returns flowOf(VolumeUnit.Ml)

        addWaterUseCase(200)

        coVerify { waterLogRepository.addWaterLog(match { it.amountMl == 200 }) }
    }

    @Test
    fun `converts oz to ml before saving`() = runTest {
        coEvery { settingsPreferences.volumeUnitFlow } returns flowOf(VolumeUnit.Oz)

        addWaterUseCase(1)

        coVerify { waterLogRepository.addWaterLog(match { it.amountMl == 30 }) }
    }

    @Test
    fun `adds water log only once`() = runTest {
        coEvery { settingsPreferences.volumeUnitFlow } returns flowOf(VolumeUnit.Ml)

        addWaterUseCase(200)

        coVerify(exactly = 1) { waterLogRepository.addWaterLog(any()) }
    }
}