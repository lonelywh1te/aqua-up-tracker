package ru.lonelywh1te.aquaup.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ru.lonelywh1te.aquaup.R
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.model.settings.WaterGoal
import ru.lonelywh1te.aquaup.presentation.home.HomeScreenEvent
import ru.lonelywh1te.aquaup.presentation.ui.components.AppSection
import ru.lonelywh1te.aquaup.presentation.ui.components.ValueListItem
import ru.lonelywh1te.aquaup.presentation.ui.components.charts.barchart.BarChart
import ru.lonelywh1te.aquaup.presentation.ui.components.charts.barchart.BarChartConfig
import ru.lonelywh1te.aquaup.presentation.ui.components.charts.barchart.BarChartEntry
import ru.lonelywh1te.aquaup.presentation.ui.components.charts.barchart.BarChartStyle
import ru.lonelywh1te.aquaup.presentation.ui.dialogs.CustomDatePickerDialog
import ru.lonelywh1te.aquaup.presentation.ui.dialogs.NumberInputDialog
import ru.lonelywh1te.aquaup.presentation.ui.dialogs.TimeInputDialog
import ru.lonelywh1te.aquaup.presentation.ui.theme.AquaUpTheme
import ru.lonelywh1te.aquaup.presentation.ui.utils.toDateString
import ru.lonelywh1te.aquaup.presentation.ui.utils.toRelativeDateString
import ru.lonelywh1te.aquaup.presentation.ui.utils.valueStringRes
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = koinViewModel<HistoryViewModel>()
) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is HistoryScreenState.Success -> {
            val successState = state as HistoryScreenState.Success
            HistoryContent(
                modifier = modifier,
                waterLogs = successState.waterLogs,
                volumeUnit = successState.volumeUnit,
                historyDate = successState.historyDate,
                waterGoal = successState.waterGoal,
                weekStart = successState.weekStart,
                chartData = successState.chartData,
                onEvent = viewModel::onEvent
            )
        }

        is HistoryScreenState.Loading -> HistoryLoading()
        is HistoryScreenState.Error -> TODO()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryContent(
    modifier: Modifier = Modifier,
    historyDate: LocalDate,
    waterLogs: List<WaterLog>,
    volumeUnit: VolumeUnit,
    waterGoal: WaterGoal,
    weekStart: LocalDate,
    chartData: List<BarChartEntry>,
    onEvent: (HistoryScreenEvent) -> Unit,
) {
    var editWaterLog by remember { mutableStateOf<WaterLog?>(null) }
    var showHistoryDatePicker by remember { mutableStateOf(false)}

    Column(
        modifier = modifier
            .padding(horizontal = dimensionResource(R.dimen.screen_padding))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AppSection(
            headerContent = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onEvent(HistoryScreenEvent.SetChartPreviousWeek) }) {
                        Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_left), contentDescription = null)
                    }

                    Text("${weekStart.toDateString()} - ${weekStart.plusDays(6).toDateString()}")

                    IconButton(onClick = { onEvent(HistoryScreenEvent.SetChartNextWeek) }) {
                        Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_right), contentDescription = null)
                    }
                }
            },
        ) {
            BarChart(
                modifier = Modifier
                    .height(300.dp),
                data = chartData,
                config = BarChartConfig(
                    target = waterGoal.value.toFloat(),
                    customMaxY = waterGoal.value.toFloat(),
                ),
                style = BarChartStyle.default.copy(
                    barColor = MaterialTheme.colorScheme.inversePrimary,
                    barCornerRadius = 16.dp,
                    textStyle = MaterialTheme.typography.labelSmall,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    axisColor = MaterialTheme.colorScheme.onSurface,
                    gridColor = MaterialTheme.colorScheme.surfaceVariant,
                    targetColor = MaterialTheme.colorScheme.tertiary
                )
            )
        }

        AppSection(
            title = stringResource(R.string.history),
            value = historyDate.toRelativeDateString(),
            onValueClick = { showHistoryDatePicker = true }
        ) {
            HistoryList(
                list = waterLogs,
                volumeUnit = stringResource(volumeUnit.valueStringRes()),
                onEditButtonClick = { waterLog ->
                    editWaterLog = waterLog
                }
            )
        }

        if (editWaterLog != null) {
            WaterLogEditorBottomSheet(
                waterLog = editWaterLog!!,
                volumeUnit = volumeUnit,
                onSave = { waterLog ->
                    onEvent(HistoryScreenEvent.WaterLogChanged(waterLog))
                },
                onDelete = { waterLog ->
                    onEvent(HistoryScreenEvent.WaterLogDeleted(waterLog))
                },
                onDismiss = {
                    editWaterLog = null
                }
            )
        }

        if (showHistoryDatePicker) {
            CustomDatePickerDialog(
                onDateSelected = {
                    onEvent(HistoryScreenEvent.HistoryDateChanged(it))
                },
                onDismiss = {
                    showHistoryDatePicker = false
                }
            )
        }

        Spacer(Modifier.height(dimensionResource(R.dimen.base_spacing)))
    }
}

@Composable
fun HistoryLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterLogEditorBottomSheet(
    waterLog: WaterLog,
    volumeUnit: VolumeUnit,
    onSave: (WaterLog) -> Unit,
    onDelete: (WaterLog) -> Unit,
    onDismiss: () -> Unit,
) {
    val isPreview = LocalInspectionMode.current
    val sheetState = rememberStandardBottomSheetState(
        skipHiddenState = false,
        initialValue = if (isPreview) SheetValue.Expanded else SheetValue.Hidden
    )

    var editedWaterLog by remember { mutableStateOf(waterLog) }

    var isWaterInputDialogVisible by remember { mutableStateOf(false) }
    var isTimeInputDialogVisible by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.screen_padding))
                .fillMaxWidth()
        ) {
            AppSection {
                Column {
                    ValueListItem(
                        label = "Объём",
                        value = "${editedWaterLog.amountMl} ${stringResource(volumeUnit.valueStringRes())}",
                        onClick = { isWaterInputDialogVisible = true }
                    )

                    HorizontalDivider()

                    ValueListItem(
                        label = "Время",
                        value = editedWaterLog.timestamp.toLocalTime().toRelativeDateString(),
                        onClick = { isTimeInputDialogVisible = true }
                    )
                }
            }

            Spacer(Modifier.height(dimensionResource(R.dimen.base_spacing)))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                    ),
                    modifier = Modifier.size(56.dp),
                    onClick = {
                        onDelete(editedWaterLog)
                        onDismiss()
                    }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                        contentDescription = null
                    )
                }

                Spacer(Modifier.width(dimensionResource(R.dimen.base_spacing)))

                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    onClick = {
                        onSave(editedWaterLog)
                        onDismiss()
                    }
                ) {
                    Text("Сохранить")
                }
            }
        }


        if (isWaterInputDialogVisible) {
            NumberInputDialog(
                title = stringResource(R.string.enter_volume),
                initValue = editedWaterLog.amountMl,
                onConfirm = { value ->
                    editedWaterLog = editedWaterLog.copy(amountMl = value)

                    isWaterInputDialogVisible = false
                },
                onDismiss = { isWaterInputDialogVisible = false }
            )
        }
        
        if (isTimeInputDialogVisible) {
            TimeInputDialog(
                initTime = editedWaterLog.timestamp.toLocalTime(),
                onConfirm = { localTime ->
                    val timestamp = editedWaterLog.timestamp
                        .withHour(localTime.hour)
                        .withMinute(localTime.minute)

                    editedWaterLog = editedWaterLog.copy(timestamp = timestamp)

                    isTimeInputDialogVisible = false
                },
                onDismiss = {
                    isTimeInputDialogVisible = false
                }
            )
        }
    }
}


@Composable
fun HistoryList(
    modifier: Modifier = Modifier,
    volumeUnit: String,
    list: List<WaterLog>,
    onEditButtonClick: (WaterLog) -> Unit,
) {
    Column(modifier = modifier) {
        if (list.isEmpty()) {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.empty_list)
            )
        }

        list.forEachIndexed { index, waterLog ->
            HistoryItem(
                volume = waterLog.amountMl,
                volumeUnit = volumeUnit,
                time = waterLog.timestamp.toLocalTime(),
                onEditButtonClick = { onEditButtonClick(waterLog) }
            )

            if (index < list.lastIndex) HorizontalDivider()
        }
    }
}

@Composable
fun HistoryItem(
    modifier: Modifier = Modifier,
    volume: Int,
    volumeUnit: String,
    time: LocalTime,
    onEditButtonClick: () -> Unit
) {
    Row(
        modifier = modifier.padding(vertical = dimensionResource(R.dimen.small_spacing)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.base_spacing)),
            imageVector = ImageVector.vectorResource(R.drawable.ic_water),
            contentDescription = null
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(text = "$volume $volumeUnit")
            Text(modifier = Modifier.alpha(0.5f), text = time.toRelativeDateString())
        }

        IconButton(
            onClick = onEditButtonClick,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_edit),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun WaterLogEditorBottomSheetPreview() {
    AquaUpTheme { 
        WaterLogEditorBottomSheet(
            waterLog = WaterLog(amountMl = 300, timestamp = LocalDateTime.now()),
            onSave = {},
            onDelete = {},
            onDismiss = {},
            volumeUnit = VolumeUnit.Ml
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HistoryScreenPreview() {
    AquaUpTheme {
        Scaffold { innerPadding ->
            val state = HistoryScreenState.Success.preview

            HistoryContent(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(innerPadding),
                waterLogs = state.waterLogs,
                volumeUnit = state.volumeUnit,
                historyDate = state.historyDate,
                waterGoal = state.waterGoal,
                weekStart = state.weekStart,
                chartData = state.chartData,
                onEvent = { },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryItemPreview() {
    AquaUpTheme {
        HistoryItem(
            volume = 200,
            volumeUnit = "ml",
            time = LocalTime.of(22, 0),
            onEditButtonClick = {}
        )
    }
}