package ru.lonelywh1te.aquaup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import ru.lonelywh1te.aquaup.ui.theme.AquaUpTheme

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
}