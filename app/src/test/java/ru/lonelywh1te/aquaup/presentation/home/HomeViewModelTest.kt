package ru.lonelywh1te.aquaup.presentation.home

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
import ru.lonelywh1te.aquaup.domain.usecase.AddWaterUseCase
import ru.lonelywh1te.aquaup.domain.usecase.GetTodayWaterLogsUseCase
import ru.lonelywh1te.aquaup.presentation.ui.utils.asStringRes
import java.io.IOException

class HomeViewModelTest {
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    private lateinit var settingsPreferences: SettingsPreferences
    private lateinit var getTodayWaterLogsUseCase: GetTodayWaterLogsUseCase
    private lateinit var addWaterUseCase: AddWaterUseCase

    private lateinit var homeViewModel: HomeViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        settingsPreferences = mockk()
        getTodayWaterLogsUseCase = mockk()
        addWaterUseCase = mockk()

        every { settingsPreferences.volumeUnitFlow } returns flowOf(VolumeUnit.Ml)
        every { settingsPreferences.waterGoalFlow } returns flowOf(WaterGoal(2000))
        every { getTodayWaterLogsUseCase() } returns flowOf(Result.Loading)

        homeViewModel = HomeViewModel(settingsPreferences, getTodayWaterLogsUseCase, addWaterUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    inner class state {

        @Test
        fun `has Loading init state`() = runTest {
            val expected = HomeScreenState.Loading
            val actual = homeViewModel.state.first()

            assertEquals(expected, actual)
        }

        @Test
        fun `state is Success with correct data when all data loaded successfully`() = runTest {
            val waterLogs = listOf(
                WaterLog(amountMl = 200),
                WaterLog(amountMl = 200),
                WaterLog(amountMl = 300)
            )

            every { getTodayWaterLogsUseCase() } returns flowOf(
                Result.Loading, Result.Success(waterLogs)
            )

            homeViewModel = HomeViewModel(settingsPreferences, getTodayWaterLogsUseCase, addWaterUseCase)

            val expected = HomeScreenState.Success(
                waterGoal = 2000,
                waterAmount = 700,
                volumeUnit = VolumeUnit.Ml,
                recentWaterVolumes = listOf(200, 300)
            )

            val actual = homeViewModel.state.first { it is HomeScreenState.Success }

            assertEquals(expected, actual)
        }

        @Test
        fun `state is Error when data loading fails`() = runTest {
            val error = AppError.Unknown(IOException())

            every { getTodayWaterLogsUseCase() } returns flowOf(
                Result.Loading, Result.Failure(error)
            )

            homeViewModel = HomeViewModel(settingsPreferences, getTodayWaterLogsUseCase, addWaterUseCase)

            val expected = HomeScreenState.Error(
                messageStringRes = error.asStringRes(),
                exceptionDetails = error.e.toString()
            )
            val actual = homeViewModel.state.first { it is HomeScreenState.Error }

            assertEquals(expected, actual)
        }

    }

    @Nested
    inner class onEvent {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `addWater event calls addWaterUseCase`() = runTest {
            coEvery { addWaterUseCase(50) } just Runs

            homeViewModel.onEvent(HomeScreenEvent.AddWater(50))
            advanceUntilIdle()

            coVerify(exactly = 1) { addWaterUseCase(50) }
        }

    }

}