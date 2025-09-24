package ru.lonelywh1te.aquaup.domain.usecase

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import ru.lonelywh1te.aquaup.domain.repository.WaterLogRepository

class DeleteWaterLogUseCaseTest {
    private lateinit var waterLogRepository: WaterLogRepository

    private lateinit var deleteWaterLogUseCase: DeleteWaterLogUseCase

    @BeforeEach
    fun setUp() {
        waterLogRepository = mockk(relaxed = true)
        deleteWaterLogUseCase = DeleteWaterLogUseCase(waterLogRepository)
    }

    @Test
    fun `calls repository with given waterLog`() = runTest {
        val waterLog = WaterLog(amountMl = 250)

        deleteWaterLogUseCase(waterLog)

        coVerify(exactly = 1) { waterLogRepository.deleteWaterLog(waterLog) }
    }
}