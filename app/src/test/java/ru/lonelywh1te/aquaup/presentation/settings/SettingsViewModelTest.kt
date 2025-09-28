package ru.lonelywh1te.aquaup.presentation.settings

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import ru.lonelywh1te.aquaup.domain.model.ReminderSchedule
import ru.lonelywh1te.aquaup.domain.model.settings.AppTheme
import ru.lonelywh1te.aquaup.domain.model.settings.ReminderInterval
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.model.settings.WaterGoal
import ru.lonelywh1te.aquaup.domain.storage.SettingsPreferences
import ru.lonelywh1te.aquaup.presentation.reminder.WaterReminder
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {
    private lateinit var settingsPreferences: SettingsPreferences
    private lateinit var waterReminder: WaterReminder

    private lateinit var settingsViewModel: SettingsViewModel

    @BeforeEach
    fun setUp() {
        settingsPreferences = mockk()
        waterReminder = mockk()

        every { settingsPreferences.volumeUnitFlow } returns flowOf(VolumeUnit.Ml)
        every { settingsPreferences.waterGoalFlow } returns flowOf(WaterGoal(2000))
        every { settingsPreferences.themeFlow } returns flowOf(AppTheme.System)
        every { settingsPreferences.reminderSchedule } returns flowOf(
            ReminderSchedule(interval = ReminderInterval.EveryOneHour)
        )
        coEvery { settingsPreferences.setWaterGoal(any()) } just Runs
        coEvery { settingsPreferences.setTheme(any()) } just Runs
        coEvery { settingsPreferences.setReminderSchedule(any()) } just Runs
        coEvery { settingsPreferences.setVolumeUnit(any()) } just Runs

        every { waterReminder.setSchedule(any()) } just Runs

        settingsViewModel = SettingsViewModel(settingsPreferences, waterReminder)
    }

    @Nested
    inner class state {

        @Test
        fun `init state is Loading`() = runTest {
            val expected = SettingsScreenState.Loading
            val actual = settingsViewModel.state.first()

            assertEquals(expected, actual)
        }

        @Test
        fun `state is Success with correct data when all data loaded successfully`() = runTest {
            val expected = SettingsScreenState.Success(
                volumeUnit = VolumeUnit.Ml,
                waterGoal = WaterGoal(2000),
                theme = AppTheme.System,
                reminderSchedule = ReminderSchedule(interval = ReminderInterval.EveryOneHour),
            )

            val actual = settingsViewModel.state.first { it is SettingsScreenState.Success }

            assertEquals(expected, actual)
        }

    }

    @Nested
    inner class onEvent {

        @Test
        fun `VolumeUnitChanged event calls setVolumeUnit`() {
            val volumeUnit = VolumeUnit.Ml

            settingsViewModel.onEvent(SettingsScreenEvent.VolumeUnitChanged(volumeUnit))

            coVerify(exactly = 1) { settingsPreferences.setVolumeUnit(volumeUnit) }
        }

        @Test
        fun `ThemeChanged event calls setTheme`() {
            val theme = AppTheme.System

            settingsViewModel.onEvent(SettingsScreenEvent.ThemeChanged(theme))

            coVerify(exactly = 1) { settingsPreferences.setTheme(theme) }
        }

        @Test
        fun `WaterGoalChanged event calls setWaterGoal`() {
            val waterGoal = WaterGoal(300)

            settingsViewModel.onEvent(SettingsScreenEvent.WaterGoalChanged(waterGoal))

            coVerify(exactly = 1) { settingsPreferences.setWaterGoal(waterGoal) }
        }

        @Test
        fun `ReminderScheduleChanged event updates reminder schedule`() {
            val schedule = ReminderSchedule(
                interval = ReminderInterval.EveryTwoHours
            )

            settingsViewModel.onEvent(SettingsScreenEvent.ReminderScheduleChanged(schedule))

            coVerify(exactly = 1) { settingsPreferences.setReminderSchedule(schedule) }
            verify(exactly = 1) { waterReminder.setSchedule(schedule) }
        }

        @Test
        fun `ReminderTimesChanged event updates schedule with new times when state is Success`() = runTest {
            val successState = settingsViewModel.state.first { it is SettingsScreenState.Success } as SettingsScreenState.Success

            val schedule = successState.reminderSchedule
            val times = listOf(
                LocalTime.of(0, 0), LocalTime.of(12, 0)
            )

            settingsViewModel.onEvent(SettingsScreenEvent.ReminderTimesChanged(times))

            coVerify(exactly = 1) { settingsPreferences.setReminderSchedule(schedule.copy(times = times)) }
            verify(exactly = 1) { waterReminder.setSchedule(schedule.copy(times = times)) }
        }

        @Test
        fun `ReminderTimesChanged event do nothing state is not Success`() = runTest {
            val times = listOf(LocalTime.of(0, 0), LocalTime.of(12, 0))

            settingsViewModel.onEvent(SettingsScreenEvent.ReminderTimesChanged(times))

            coVerify(exactly = 0) { settingsPreferences.setReminderSchedule(any()) }
            verify(exactly = 0) { waterReminder.setSchedule(any()) }
        }
    }

}