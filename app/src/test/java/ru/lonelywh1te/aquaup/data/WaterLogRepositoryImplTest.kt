package ru.lonelywh1te.aquaup.data

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import ru.lonelywh1te.aquaup.data.source.db.water_log.WaterLogDao
import ru.lonelywh1te.aquaup.data.source.db.water_log.WaterLogEntity
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import ru.lonelywh1te.aquaup.domain.repository.WaterLogRepository
import ru.lonelywh1te.aquaup.domain.result.AppError
import ru.lonelywh1te.aquaup.domain.result.Result
import utils.WaterLogEntityTestFactory
import utils.WaterLogTestFactory
import java.time.LocalDateTime

class WaterLogRepositoryImplTest {
    private lateinit var waterLogDao:  WaterLogDao
    private lateinit var waterLogRepository: WaterLogRepository

    private val start = LocalDateTime.of(2025, 1, 1, 0, 0)
    private val end = LocalDateTime.of(2025, 1, 31, 23, 59)

    @BeforeEach
    fun setUp() {
        waterLogDao = mockk()
        waterLogRepository = WaterLogRepositoryImpl(waterLogDao)
    }

    @Nested
    inner class getWaterLogsForPeriod {
        @Test
        fun `return Result_Success with mapped entities`() = runTest {
            val flow = flowOf(WaterLogEntityTestFactory.createList(0, 3))
            every { waterLogDao.getWaterLogsForPeriod(start, end) } returns flow

            val expected = Result.Success(WaterLogEntityTestFactory.createList(0, 3).map { it.toWaterLog() })
            val actual = waterLogRepository.getWaterLogsForPeriod(start, end).first()

            verify(exactly = 1) { waterLogDao.getWaterLogsForPeriod(start, end) }
            assertEquals(expected, actual)
        }

        @Test
        fun `return Result_Success with empty list`() = runTest {
            val flow = flowOf(emptyList<WaterLogEntity>())
            every { waterLogDao.getWaterLogsForPeriod(start, end) } returns flow

            val expected = Result.Success(emptyList<WaterLog>())
            val actual = waterLogRepository.getWaterLogsForPeriod(start, end).first()

            verify(exactly = 1) { waterLogDao.getWaterLogsForPeriod(start, end) }
            assertEquals(expected, actual)
        }

        @Test
        fun `handle database errors and return Result_Failure`() = runTest {
            val exception = Exception()
            val flow = flow<List<WaterLogEntity>> { throw exception }
            every { waterLogDao.getWaterLogsForPeriod(start, end) } returns flow

            val expected = Result.Failure(AppError.Unknown(exception))
            val actual = waterLogRepository.getWaterLogsForPeriod(start, end).first()

            verify(exactly = 1) { waterLogDao.getWaterLogsForPeriod(start, end) }
            assertEquals(expected, actual)
        }
    }

    @Nested
    inner class addWaterLog {

        @Test
        fun `return Result_Success with Unit`() = runTest {
            val waterLog = WaterLogTestFactory.create(0)
            coEvery { waterLogDao.addWaterLog(waterLog.toWaterLogEntity()) } just Runs

            val expected = Result.Success(Unit)
            val actual = waterLogRepository.addWaterLog(waterLog)

            coVerify(exactly = 1) { waterLogDao.addWaterLog(waterLog.toWaterLogEntity()) }

            assertEquals(expected, actual)
        }

        @Test
        fun `handle database errors and return Result_Failure`() = runTest {
            val exception = Exception()
            val waterLog = WaterLogTestFactory.create(0)

            coEvery { waterLogDao.addWaterLog(waterLog.toWaterLogEntity()) } throws exception

            val expected = Result.Failure(AppError.Unknown(exception))
            val actual = waterLogRepository.addWaterLog(waterLog)

            coVerify(exactly = 1) { waterLogDao.addWaterLog(waterLog.toWaterLogEntity()) }

            assertEquals(expected, actual)
        }

    }

    @Nested
    inner class deleteWaterLog {
        @Test
        fun `return Result_Success with Unit`() = runTest {
            val waterLog = WaterLogTestFactory.create(0)
            coEvery { waterLogDao.deleteWaterLog(waterLog.toWaterLogEntity()) } just Runs

            val expected = Result.Success(Unit)
            val actual = waterLogRepository.deleteWaterLog(waterLog)

            coVerify(exactly = 1) { waterLogDao.deleteWaterLog(waterLog.toWaterLogEntity()) }

            assertEquals(expected, actual)
        }

        @Test
        fun `handle database errors and return Result_Failure`() = runTest {
            val exception = Exception()
            val waterLog = WaterLogTestFactory.create(0)

            coEvery { waterLogDao.deleteWaterLog(waterLog.toWaterLogEntity()) } throws exception

            val expected = Result.Failure(AppError.Unknown(exception))
            val actual = waterLogRepository.deleteWaterLog(waterLog)

            coVerify(exactly = 1) { waterLogDao.deleteWaterLog(waterLog.toWaterLogEntity()) }

            assertEquals(expected, actual)
        }
    }

    @Nested
    inner class updateWaterLog {
        @Test
        fun `return Result_Success with Unit`() = runTest {
            val waterLog = WaterLogTestFactory.create(0)
            coEvery { waterLogDao.updateWaterLog(waterLog.toWaterLogEntity()) } just Runs

            val expected = Result.Success(Unit)
            val actual = waterLogRepository.updateWaterLog(waterLog)

            coVerify(exactly = 1) { waterLogDao.updateWaterLog(waterLog.toWaterLogEntity()) }

            assertEquals(expected, actual)
        }

        @Test
        fun `handle database errors and return Result_Failure`() = runTest {
            val exception = Exception()
            val waterLog = WaterLogTestFactory.create(0)

            coEvery { waterLogDao.updateWaterLog(waterLog.toWaterLogEntity()) } throws exception

            val expected = Result.Failure(AppError.Unknown(exception))
            val actual = waterLogRepository.updateWaterLog(waterLog)

            coVerify(exactly = 1) { waterLogDao.updateWaterLog(waterLog.toWaterLogEntity()) }

            assertEquals(expected, actual)
        }
    }
}