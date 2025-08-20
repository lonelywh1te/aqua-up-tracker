package ru.lonelywh1te.aquaup.presentation.settings

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
import ru.lonelywh1te.aquaup.domain.model.SettingsItem
import ru.lonelywh1te.aquaup.domain.model.settings.AppTheme
import ru.lonelywh1te.aquaup.domain.model.settings.ReminderInterval
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.model.settings.WaterGoal
import ru.lonelywh1te.aquaup.presentation.home.HomeScreenEvent
import ru.lonelywh1te.aquaup.presentation.home.HomeViewModel
import ru.lonelywh1te.aquaup.presentation.ui.components.AppSection
import ru.lonelywh1te.aquaup.presentation.ui.components.LabeledValueItem
import ru.lonelywh1te.aquaup.presentation.ui.components.SelectableListBottomSheet
import ru.lonelywh1te.aquaup.presentation.ui.dialogs.NumberInputDialog
import ru.lonelywh1te.aquaup.presentation.ui.theme.AquaUpTheme
import ru.lonelywh1te.aquaup.presentation.ui.utils.getAppVersion
import ru.lonelywh1te.aquaup.presentation.ui.utils.stringRes
import ru.lonelywh1te.aquaup.presentation.ui.utils.valueStringRes

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
    var selectedSettingsItem: SettingsItem? by remember { mutableStateOf(null) }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        AppSection(title = stringResource(R.string.main_settings)) {
            MainSettingsList(
                volumeUnit = state.volumeUnit,
                waterGoal = state.waterGoal,
                reminderInterval = state.reminderInterval,
                theme = state.theme,
                onSettingsItemClick = { item ->
                    selectedSettingsItem = item
                },
            )
        }

        AppSection(title = stringResource(R.string.about)) {
            AboutSettingsList()
        }
    }

    when (selectedSettingsItem) {
        is SettingsItem.AppTheme -> {
            val themeOptions = AppTheme.entries
            val stringItems = themeOptions.map { stringResource(it.valueStringRes()) }

            SelectableListBottomSheet(
                items = stringItems,
                onItemSelected = { item ->
                    onEvent(SettingsScreenEvent.ThemeChanged(themeOptions[stringItems.indexOf(item)]))
                    selectedSettingsItem = null
                },
                onDismiss = { selectedSettingsItem = null }
            )
        }
        is SettingsItem.ReminderInterval -> {
            /* TODO */
        }
        is SettingsItem.VolumeUnit -> {
            val volumeUnitOptions = VolumeUnit.entries
            val stringItems = volumeUnitOptions.map { stringResource(it.valueStringRes()) }

            SelectableListBottomSheet(
                items = stringItems,
                onItemSelected = { item ->
                    onEvent(SettingsScreenEvent.VolumeUnitChanged(volumeUnitOptions[stringItems.indexOf(item)]))
                    selectedSettingsItem = null
                },
                onDismiss = { selectedSettingsItem = null }
            )
        }
        is SettingsItem.WaterGoal -> {
            NumberInputDialog(
                title = stringResource(R.string.enter_volume),
                onConfirm = { value ->
                    onEvent(SettingsScreenEvent.WaterGoalChanged(WaterGoal(value)))
                    selectedSettingsItem = null
                },
                onDismiss = { selectedSettingsItem = null }
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
    reminderInterval: ReminderInterval,
    theme: AppTheme,
    onSettingsItemClick: (SettingsItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val settings = listOf(
        SettingsItem.VolumeUnit to stringResource(volumeUnit.valueStringRes()),
        SettingsItem.WaterGoal(waterGoal.value) to waterGoal.value.toString(),
        SettingsItem.ReminderInterval to stringResource(reminderInterval.valueStringRes()),
        SettingsItem.AppTheme to stringResource(theme.valueStringRes()),
    )

    Column(modifier = modifier) {
        settings.forEachIndexed { index, (item, value) ->
            LabeledValueItem(
                label = stringResource(item.stringRes()),
                value = value,
                onClick = { onSettingsItemClick(item) }
            )

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
        LabeledValueItem(stringResource(R.string.version), value = version)
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


