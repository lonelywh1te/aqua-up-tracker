package ru.lonelywh1te.aquaup.presentation.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.lonelywh1te.aquaup.domain.storage.SettingsPreferences

class SettingsViewModel(
    private val settingsPreferences: SettingsPreferences,
): ViewModel() {
    val state: StateFlow<SettingsScreenState> = combine(
        settingsPreferences.volumeUnitFlow,
        settingsPreferences.waterGoalFlow,
        settingsPreferences.themeFlow,
        settingsPreferences.reminderSchedule,
    ) { volume, waterGoal, theme, reminderSchedule ->
        SettingsScreenState.Success(volume, waterGoal, theme, reminderSchedule)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsScreenState.Loading)


    fun onEvent(event: SettingsScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is SettingsScreenEvent.VolumeUnitChanged -> {
                    settingsPreferences.setVolumeUnit(event.value)
                }
                is SettingsScreenEvent.ReminderScheduleChanged -> {
                    settingsPreferences.setReminderSchedule(event.value)
                }
                is SettingsScreenEvent.ReminderTimesChanged -> {
                    if (state.value is SettingsScreenState.Success) {
                        val schedule = (state.value as SettingsScreenState.Success).reminderSchedule.copy(times = event.value)
                        settingsPreferences.setReminderSchedule(schedule)
                    }
                }
                is SettingsScreenEvent.ThemeChanged -> {
                    settingsPreferences.setTheme(event.value)
                }
                is SettingsScreenEvent.WaterGoalChanged -> {
                    settingsPreferences.setWaterGoal(event.value)
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            state.collect { state ->
                Log.d("SettingsViewModel", state.toString())
            }
        }
    }
}