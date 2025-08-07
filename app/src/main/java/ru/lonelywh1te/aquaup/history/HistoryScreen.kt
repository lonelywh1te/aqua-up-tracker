package ru.lonelywh1te.aquaup.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.lonelywh1te.aquaup.R
import ru.lonelywh1te.aquaup.core.ui.components.AppSection
import ru.lonelywh1te.aquaup.core.ui.theme.AquaUpTheme
import ru.lonelywh1te.aquaup.history.domain.HistoryData
import java.time.LocalTime

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    state: HistoryScreenState,
    onEditHistoryDataClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = dimensionResource(R.dimen.screen_padding))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AppSection(title = stringResource(R.string.history)) {
            HistoryList(
                volumeUnit = state.volumeUnit.uiName,
                list = state.historyData,
                onEditButtonClick = onEditHistoryDataClick
            )
        }
        
        Spacer(Modifier.height(dimensionResource(R.dimen.base_spacing)))
    }
}

@Composable
fun HistoryList(
    modifier: Modifier = Modifier,
    volumeUnit: String,
    list: List<HistoryData>,
    onEditButtonClick: () -> Unit,
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

        list.forEachIndexed { index, historyData ->
            HistoryItem(
                volume = historyData.volume,
                volumeUnit = volumeUnit,
                time = historyData.time,
                onEditButtonClick = onEditButtonClick
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
            Text(modifier = Modifier.alpha(0.5f), text = time.toString())
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HistoryScreenEmptyListPreview() {
    AquaUpTheme {
        Scaffold { innerPadding ->
            HistoryScreen(
                modifier = Modifier.padding(innerPadding),
                state = HistoryScreenState.getPreviewState().copy(historyData = listOf()),
                onEditHistoryDataClick = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HistoryScreenPreview() {
    AquaUpTheme {
        Scaffold { innerPadding ->
            HistoryScreen(
                modifier = Modifier.padding(innerPadding),
                state = HistoryScreenState.getPreviewState(),
                onEditHistoryDataClick = {}
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