package ru.lonelywh1te.aquaup.presentation.history

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.model.settings.WaterGoal
import ru.lonelywh1te.aquaup.domain.result.AppError
import ru.lonelywh1te.aquaup.domain.result.Result
import ru.lonelywh1te.aquaup.domain.storage.SettingsPreferences
import ru.lonelywh1te.aquaup.domain.usecase.DeleteWaterLogUseCase
import ru.lonelywh1te.aquaup.domain.usecase.GetWaterLogsByDateUseCase
import ru.lonelywh1te.aquaup.domain.usecase.GetWeeklyWaterLogsUseCase
import ru.lonelywh1te.aquaup.domain.usecase.UpdateWaterLogUseCase
import ru.lonelywh1te.aquaup.presentation.ui.components.charts.barchart.BarChartEntry
import ru.lonelywh1te.aquaup.presentation.ui.utils.asStringRes
import java.io.IOException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModelTest {
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    private lateinit var settingsPreferences: SettingsPreferences
    private lateinit var  getWaterLogsByDateUseCase: GetWaterLogsByDateUseCase
    private lateinit var  getWeeklyWaterLogsUseCase: GetWeeklyWaterLogsUseCase
    private lateinit var  updateWaterLogUseCase: UpdateWaterLogUseCase
    private lateinit var  deleteWaterLogUseCase: DeleteWaterLogUseCase
    private lateinit var  locale: Locale

    private lateinit var historyViewModel: HistoryViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        settingsPreferences = mockk()
        getWaterLogsByDateUseCase = mockk()
        getWeeklyWaterLogsUseCase = mockk()
        updateWaterLogUseCase = mockk()
        deleteWaterLogUseCase = mockk()
        locale = Locale.US

        every { settingsPreferences.volumeUnitFlow } returns flowOf(VolumeUnit.Oz)
        every { settingsPreferences.waterGoalFlow } returns flowOf(WaterGoal(2000))
        coEvery { updateWaterLogUseCase(any()) } just Runs
        coEvery { deleteWaterLogUseCase(any()) } just Runs

        historyViewModel = HistoryViewModel(
            settingsPreferences,
            getWaterLogsByDateUseCase,
            getWeeklyWaterLogsUseCase,
            updateWaterLogUseCase,
            deleteWaterLogUseCase,
            locale
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    inner class state {

        @Test
        fun `init state is Loading`() = runTest {
            val expected = HistoryScreenState.Loading
            val actual = historyViewModel.state.first()

            assertEquals(expected, actual)
        }

        @Test
        fun `state is Success with correct data when all data loaded successfully`() = runTest {
            val date = LocalDate.now()
            val monday = date.with(DayOfWeek.MONDAY)

            val waterLog = WaterLog(amountMl = 200, timestamp = monday.atStartOfDay())
            val waterLogs = listOf(waterLog)
            val weeklyLogs = mapOf(
                monday to waterLogs,
                monday.plusDays(1) to emptyList(),
                monday.plusDays(2) to emptyList(),
                monday.plusDays(3) to emptyList(),
                monday.plusDays(4) to emptyList(),
                monday.plusDays(5) to emptyList(),
                monday.plusDays(6) to emptyList(),
            )

            every { getWaterLogsByDateUseCase(any()) } returns flowOf(
                Result.Loading, Result.Success(waterLogs)
            )

            every { getWeeklyWaterLogsUseCase(any()) } returns flowOf(
                Result.Loading, Result.Success(weeklyLogs)
            )

            val expected = HistoryScreenState.Success(
                volumeUnit = VolumeUnit.Oz,
                waterLogs = waterLogs,
                historyDate = date,
                weekStart = monday,
                waterGoal = WaterGoal(2000),
                chartData = weeklyLogs.map { (date, logs) ->
                    BarChartEntry(
                        x = date.atStartOfDay().dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.US),
                        y = logs.sumOf { it.amountMl }.toFloat()
                    )
                },
            )

            val actual = historyViewModel.state.first { it is HistoryScreenState.Success }

            assertEquals(expected, actual)
        }

        @Test
        fun `state is Error when data loading fails`() = runTest {
            val error = AppError.Unknown(IOException())

            every { getWaterLogsByDateUseCase(any()) } returns flowOf(
                Result.Loading
            )

            every { getWeeklyWaterLogsUseCase(any()) } returns flowOf(
                Result.Loading, Result.Failure(error)
            )

            val expected = HistoryScreenState.Error(
                messageStringRes = error.asStringRes(),
                exceptionDetails = error.e.toString()
            )
            val actual = historyViewModel.state.first { it is HistoryScreenState.Error }

            assertEquals(expected, actual)
        }

    }

    @Nested
    inner class onEvent {


        @Test
        fun `WaterLogChanged event calls updateWaterLogUseCase`() = runTest {
            val waterLog = WaterLog(amountMl = 350)

            historyViewModel.onEvent(HistoryScreenEvent.WaterLogChanged(waterLog))
            advanceUntilIdle()

            coVerify(exactly = 1) { updateWaterLogUseCase(waterLog) }
        }

        @Test
        fun `WaterLogDeleted event calls deleteWaterLogUseCase`() = runTest {
            val waterLog = WaterLog(amountMl = 350)

            historyViewModel.onEvent(HistoryScreenEvent.WaterLogDeleted(waterLog))
            advanceUntilIdle()

            coVerify(exactly = 1) { deleteWaterLogUseCase(waterLog) }
        }

        @Test
        fun `HistoryDateChanged update history date`() = runTest {
            val date = LocalDate.of(2025, 9, 28)

            every { getWaterLogsByDateUseCase(any()) } returns flowOf(Result.Success(emptyList()))
            every { getWeeklyWaterLogsUseCase(any()) } returns flowOf(Result.Success(emptyMap()))

            historyViewModel.onEvent(HistoryScreenEvent.HistoryDateChanged(date))
            advanceUntilIdle()

            val expected = date
            val successState = historyViewModel.state.first { it is HistoryScreenState.Success } as HistoryScreenState.Success
            val actual = successState.historyDate

            assertEquals(expected, actual)
        }

        @Test
        fun `SetChartNextWeek plus week to week start`() = runTest {
            every { getWaterLogsByDateUseCase(any()) } returns flowOf(Result.Success(emptyList()))
            every { getWeeklyWaterLogsUseCase(any()) } returns flowOf(Result.Success(emptyMap()))

            historyViewModel.onEvent(HistoryScreenEvent.SetChartNextWeek)
            advanceUntilIdle()

            val expected = LocalDate.now().with(DayOfWeek.MONDAY).plusWeeks(1)
            val successState = historyViewModel.state.first { it is HistoryScreenState.Success } as HistoryScreenState.Success
            val actual = successState.weekStart

            assertEquals(expected, actual)
        }

        @Test
        fun `SetChartNextWeek minus week to week start`() = runTest {
            every { getWaterLogsByDateUseCase(any()) } returns flowOf(Result.Success(emptyList()))
            every { getWeeklyWaterLogsUseCase(any()) } returns flowOf(Result.Success(emptyMap()))

            historyViewModel.onEvent(HistoryScreenEvent.SetChartPreviousWeek)
            advanceUntilIdle()

            val expected = LocalDate.now().with(DayOfWeek.MONDAY).minusWeeks(1)
            val successState = historyViewModel.state.first { it is HistoryScreenState.Success } as HistoryScreenState.Success
            val actual = successState.weekStart

            assertEquals(expected, actual)
        }

    }

}