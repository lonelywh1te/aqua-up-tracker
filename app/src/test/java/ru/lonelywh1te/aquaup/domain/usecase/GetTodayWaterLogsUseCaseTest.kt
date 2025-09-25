package ru.lonelywh1te.aquaup.domain.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.lonelywh1te.aquaup.domain.result.AppError
import ru.lonelywh1te.aquaup.domain.result.Result
import utils.WaterLogTestFactory
import java.time.LocalDate

class GetTodayWaterLogsUseCaseTest {
    private lateinit var getWaterLogsByDateUseCase: GetWaterLogsByDateUseCase

    private lateinit var getTodayWaterLogsUseCase: GetTodayWaterLogsUseCase

    private val today = LocalDate.now()

    @BeforeEach
    fun setUp(){
        getWaterLogsByDateUseCase = mockk()
        getTodayWaterLogsUseCase = GetTodayWaterLogsUseCase(getWaterLogsByDateUseCase)
    }

    @Test
    fun `calls GetWaterLogsByDateUseCase with today's date`() = runTest {
        val flow = flowOf(Result.Success(listOf(WaterLogTestFactory.create(1))))
        every { getWaterLogsByDateUseCase(LocalDate.now()) } returns flow

        val expected = flow.first()
        val actual = getTodayWaterLogsUseCase().first()

        assertEquals(expected, actual)
        verify(exactly = 1) { getWaterLogsByDateUseCase(today) }
    }

    @Test
    fun `propagates failure from GetWaterLogsByDateUseCase`() = runTest {
        val error = Result.Failure(AppError.Unknown(RuntimeException("db error")))
        every { getWaterLogsByDateUseCase(today) } returns flowOf(error)

        val expected = error
        val actual = getTodayWaterLogsUseCase().first()

        assertEquals(expected, actual)
    }
}