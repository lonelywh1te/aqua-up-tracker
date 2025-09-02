package ru.lonelywh1te.aquaup.presentation.settings

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ru.lonelywh1te.aquaup.R
import ru.lonelywh1te.aquaup.domain.model.ReminderSchedule
import ru.lonelywh1te.aquaup.domain.model.settings.AppTheme
import ru.lonelywh1te.aquaup.domain.model.settings.ReminderInterval
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.model.settings.WaterGoal
import ru.lonelywh1te.aquaup.presentation.navigation.SettingsDestination
import ru.lonelywh1te.aquaup.presentation.settings.SettingsScreenEvent.*
import ru.lonelywh1te.aquaup.presentation.ui.components.AppSection
import ru.lonelywh1te.aquaup.presentation.ui.components.CheckableListBottomSheet
import ru.lonelywh1te.aquaup.presentation.ui.components.ListItem
import ru.lonelywh1te.aquaup.presentation.ui.components.ValueListItem
import ru.lonelywh1te.aquaup.presentation.ui.components.SelectableListBottomSheet
import ru.lonelywh1te.aquaup.presentation.ui.dialogs.NumberInputDialog
import ru.lonelywh1te.aquaup.presentation.ui.theme.AquaUpTheme
import ru.lonelywh1te.aquaup.presentation.ui.utils.getAppVersion
import ru.lonelywh1te.aquaup.presentation.ui.utils.stringRes
import ru.lonelywh1te.aquaup.presentation.ui.utils.toRelativeDateString
import ru.lonelywh1te.aquaup.presentation.ui.utils.valueStringRes
import java.time.LocalTime

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel<SettingsViewModel>(),
    modifier: Modifier = Modifier
) {
    val state: SettingsScreenState by viewModel.state.collectAsState()

    when (state) {
        is SettingsScreenState.Success -> {
            val successState = state as SettingsScreenState.Success
            SettingsContent(
                modifier = modifier,
                state = successState,
                onEvent = viewModel::onEvent
            )
        }
        is SettingsScreenState.Loading -> SettingsLoading(modifier)
        is SettingsScreenState.Error -> TODO()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    state: SettingsScreenState.Success,
    onEvent: (SettingsScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var settingsDestination: SettingsDestination? by remember { mutableStateOf(null) }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        AppSection(title = stringResource(R.string.main_settings)) {
            MainSettingsList(
                volumeUnit = state.volumeUnit,
                waterGoal = state.waterGoal,
                reminderSchedule = state.reminderSchedule,
                theme = state.theme,
                onSettingsItemClick = { destination ->
                    settingsDestination = destination
                },
            )
        }

        AppSection(title = stringResource(R.string.about)) {
            AboutSettingsList()
        }
    }

    when (settingsDestination) {
        is SettingsDestination.AppTheme -> {
            SelectableListBottomSheet(
                items = AppTheme.entries,
                text = { stringResource(it.valueStringRes()) },
                onItemSelected = { selectedTheme ->
                    onEvent(ThemeChanged(selectedTheme))
                    settingsDestination = null
                },
                onDismiss = { settingsDestination = null }
            )
        }
        is SettingsDestination.ReminderSchedule -> {
            SelectableListBottomSheet(
                items = ReminderInterval.entries,
                text = { stringResource(it.valueStringRes()) },
                onItemSelected = { selectedInterval ->
                    settingsDestination = if (selectedInterval == ReminderInterval.Custom){
                        SettingsDestination.ReminderTimes
                    } else {
                        null
                    }

                    onEvent(ReminderScheduleChanged(ReminderSchedule(selectedInterval)))
                },
                onDismiss = { settingsDestination = null }
            )
        }
        is SettingsDestination.ReminderTimes -> {
            CheckableListBottomSheet(
                items = (0..23).map { LocalTime.of(it, 0) },
                checkedItems = state.reminderSchedule.times,
                text = { it.toRelativeDateString() },
                onConfirm = { times ->
                    Log.d("SettingsScreen", times.toString())
                    onEvent(ReminderTimesChanged(times))
                    settingsDestination = null
                },
                onDismiss = { settingsDestination = null }
            )
            
        }
        is SettingsDestination.VolumeUnit -> {
            SelectableListBottomSheet(
                items = VolumeUnit.entries,
                text = { stringResource(it.valueStringRes()) },
                onItemSelected = { selectedVolumeUnit ->
                    onEvent(VolumeUnitChanged(selectedVolumeUnit))
                    settingsDestination = null
                },
                onDismiss = { settingsDestination = null }
            )
        }
        is SettingsDestination.WaterGoal -> {
            NumberInputDialog(
                title = stringResource(R.string.enter_volume),
                onConfirm = { value ->
                    onEvent(WaterGoalChanged(WaterGoal(value)))
                    settingsDestination = null
                },
                onDismiss = { settingsDestination = null }
            )
        }
        null -> return
    }
}

@Composable
fun SettingsLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun MainSettingsList(
    volumeUnit: VolumeUnit,
    waterGoal: WaterGoal,
    reminderSchedule: ReminderSchedule,
    theme: AppTheme,
    onSettingsItemClick: (SettingsDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val settings = listOf(
        SettingsDestination.VolumeUnit to stringResource(volumeUnit.valueStringRes()),
        SettingsDestination.WaterGoal(waterGoal.value) to waterGoal.value.toString(),
        SettingsDestination.ReminderSchedule to stringResource(reminderSchedule.interval.valueStringRes()),
        SettingsDestination.AppTheme to stringResource(theme.valueStringRes()),
    )

    Column(modifier = modifier) {
        settings.forEachIndexed { index, (item, value) ->
            ValueListItem(
                label = stringResource(item.stringRes()),
                value = value,
                onClick = { onSettingsItemClick(item) }
            )

            if (item is SettingsDestination.ReminderSchedule && reminderSchedule.interval == ReminderInterval.Custom) {
                HorizontalDivider()
                ListItem(
                    label = stringResource(SettingsDestination.ReminderTimes.stringRes()),
                    onClick = { onSettingsItemClick(SettingsDestination.ReminderTimes) }
                )
            }

            if (index < settings.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun AboutSettingsList(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val version = remember { context.getAppVersion() }

    Column {
        ValueListItem(label = stringResource(R.string.version), value = version)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingsScreenPreview() {
    AquaUpTheme {
        Scaffold { innerPadding ->
            val state: SettingsScreenState.Success = SettingsScreenState.Success.preview

            SettingsContent(
                modifier = Modifier.padding(innerPadding),
                state = state,
                onEvent = { }
            )
        }
    }
}


