package ru.lonelywh1te.aquaup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.lonelywh1te.aquaup.core.ui.theme.AquaUpTheme
import ru.lonelywh1te.aquaup.history.HistoryScreen
import ru.lonelywh1te.aquaup.history.HistoryScreenState
import ru.lonelywh1te.aquaup.home.HomeScreen
import ru.lonelywh1te.aquaup.home.HomeScreenState

// TODO: volume unit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AquaUpTheme {
                Scaffold (
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    MainScreen(
                        modifier = Modifier
                            .padding(top = innerPadding.calculateTopPadding())
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(bottom = innerPadding.calculateBottomPadding())
                    )
                }
            }
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    private fun MainScreenPreview() {
        AquaUpTheme {
            Scaffold (
                modifier = Modifier
                    .fillMaxSize()
            ) { innerPadding ->
                MainScreen(
                    modifier = Modifier
                        .padding(top = innerPadding.calculateTopPadding())
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(bottom = innerPadding.calculateBottomPadding())
                )
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
                    state = HomeScreenState.getPreviewState(),
                    onEvent = {}
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
                    onEditHistoryDataClick = { /* TODO */ }
                )
            }
        }
    }
}