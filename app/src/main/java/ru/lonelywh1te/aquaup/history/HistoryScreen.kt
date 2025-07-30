package ru.lonelywh1te.aquaup.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.lonelywh1te.aquaup.R
import ru.lonelywh1te.aquaup.core.ui.AppSection
import ru.lonelywh1te.aquaup.history.domain.HistoryData
import ru.lonelywh1te.aquaup.core.ui.theme.AquaUpTheme
import java.time.LocalTime

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    historyDataList: List<HistoryData>,
    volumeUnit: String,
    onEditHistoryDataClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        AppSection(title = "История") {
            HistoryList(
                volumeUnit = volumeUnit,
                list = historyDataList,
                onEditButtonClick = onEditHistoryDataClick
            )
        }
        
        Spacer(Modifier.height(16.dp))
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
            Text(modifier = Modifier.padding(16.dp).fillMaxWidth(), text = "Список пуст")
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
        modifier = modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(horizontal = 16.dp),
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
                historyDataList = emptyList(),
                volumeUnit = "ml",
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
                historyDataList = listOf(
                    HistoryData(200, LocalTime.of(22, 0)),
                    HistoryData(300, LocalTime.of(22, 0)),
                    HistoryData(1100, LocalTime.of(22, 0)),
                    HistoryData(100, LocalTime.of(22, 0)),
                    HistoryData(200, LocalTime.of(22, 0)),
                    HistoryData(300, LocalTime.of(22, 0)),
                ),
                volumeUnit = "ml",
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