package ru.lonelywh1te.aquaup.presentation.settings

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
        settingsPreferences.reminderIntervalFlow,
    ) { volume, waterGoal, theme, reminder ->
        SettingsScreenState.Success(volume, waterGoal, theme, reminder)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsScreenState.Loading)


    fun onEvent(event: SettingsScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is SettingsScreenEvent.VolumeUnitChanged -> settingsPreferences.setVolumeUnit(event.value)
                is SettingsScreenEvent.ReminderIntervalChanged -> settingsPreferences.setReminderInterval(event.value)
                is SettingsScreenEvent.ThemeChanged -> settingsPreferences.setTheme(event.value)
                is SettingsScreenEvent.WaterGoalChanged -> settingsPreferences.setWaterGoal(event.value)
            }
        }
    }
}