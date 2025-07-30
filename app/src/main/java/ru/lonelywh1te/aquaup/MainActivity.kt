package ru.lonelywh1te.aquaup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.lonelywh1te.aquaup.history.domain.HistoryData
import ru.lonelywh1te.aquaup.history.HistoryScreen
import ru.lonelywh1te.aquaup.home.HomeScreen
import ru.lonelywh1te.aquaup.core.ui.theme.AquaUpTheme
import java.time.LocalTime

// TODO: volume unit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AquaUpTheme {
                Scaffold { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding),
                        waterGoal = 2000,
                        currentWaterAmount = 1200,
                        volumeUnit = "ml",
                        waterVolumes = listOf(100, 200, 300, 400, 500, 600, 800, 1000),
                        onWaterVolumeSelected = { /* TODO */ },
                        onAddWaterClick = { /* TODO */ },
                    )
                }
            }
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    private fun HomeScreenPreview() {
        AquaUpTheme {
            Scaffold { innerPadding ->
                HomeScreen(
                    modifier = Modifier.padding(innerPadding),
                    waterGoal = 2000,
                    currentWaterAmount = 1200,
                    volumeUnit = "ml",
                    waterVolumes = listOf(100, 200, 300, 400, 500, 600, 800, 1000),
                    onWaterVolumeSelected = { /* TODO */ },
                    onAddWaterClick = { /* TODO */ },
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
                    onEditHistoryDataClick = { /* TODO */ }
                )
            }
        }
    }
}