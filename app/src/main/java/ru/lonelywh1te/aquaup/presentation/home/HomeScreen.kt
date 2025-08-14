package ru.lonelywh1te.aquaup.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import ru.lonelywh1te.aquaup.R
import ru.lonelywh1te.aquaup.domain.model.VolumeUnit
import ru.lonelywh1te.aquaup.presentation.ui.dialogs.NumberInputDialog
import ru.lonelywh1te.aquaup.presentation.ui.theme.AquaUpTheme
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is HomeScreenState.Success -> {
            val successState = state as HomeScreenState.Success

            HomeContent(
                modifier = modifier,
                waterGoal = successState.waterGoal,
                waterAmount = successState.waterAmount,
                volumeUnit = successState.volumeUnit,
                recentWaterVolumes = successState.recentWaterVolumes,
                onEvent = viewModel::onEvent
            )
        }

        is HomeScreenState.Loading -> HomeLoading(modifier)

        is HomeScreenState.Error -> {
            /* TODO */
        }
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    waterGoal: Int,
    volumeUnit: VolumeUnit,
    waterAmount: Int,
    recentWaterVolumes: List<Int>,
    onEvent: (HomeScreenEvent) -> Unit
) {
    var isWaterInputDialogVisible by remember { mutableStateOf(false) }

    Column (
        modifier = modifier
            .padding(vertical = dimensionResource(R.dimen.screen_padding)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        HomeProgressOverview(
            waterGoal = waterGoal,
            volumeUnit = volumeUnit.uiName,
            progressPercentage = if (waterGoal != 0) ((waterAmount.toFloat() / waterGoal) * 100).roundToInt() else 0
        )

        HomeCurrentWaterProgress(
            amount = waterAmount,
            volumeUnit = volumeUnit.uiName,
            modifier = Modifier.weight(1f)
        )

        RecentWaterVolumes(
            volumes = recentWaterVolumes,
            volumeUnit = volumeUnit.uiName,
            onVolumeSelected = {
                onEvent(HomeScreenEvent.AddWater(it))
            }
        )

        AddWaterButton(
            Modifier.padding(horizontal = dimensionResource(R.dimen.base_spacing)),
            onAddWaterClick = { isWaterInputDialogVisible = true }
        )

        if (isWaterInputDialogVisible) {
            NumberInputDialog(
                title = stringResource(R.string.enter_volume),
                onConfirm = { value ->
                    onEvent(HomeScreenEvent.AddWater(value))
                    isWaterInputDialogVisible = false
                },
                onDismiss = { isWaterInputDialogVisible = false }
            )
        }
    }
}

@Composable
private fun HomeLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun HomeProgressOverview(
    modifier: Modifier = Modifier,
    waterGoal: Int,
    progressPercentage: Int,
    volumeUnit: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.current_goal, waterGoal, volumeUnit),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(R.string.completed_percentage, progressPercentage),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun HomeCurrentWaterProgress(
    modifier: Modifier = Modifier,
    amount: Int,
    volumeUnit: String,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.already_been_drunk),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "$amount",
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 96.sp, fontWeight = FontWeight.Medium)
        )
        Text(
            text = volumeUnit,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun RecentWaterVolumes(
    modifier: Modifier = Modifier,
    volumes: List<Int>,
    volumeUnit: String,
    onVolumeSelected: (volume: Int) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(volumes) { volume ->
            WaterVolumeButton(volume = volume, volumeUnit = volumeUnit, onClick = onVolumeSelected)
        }
    }
}

@Composable
private fun WaterVolumeButton(
    modifier: Modifier = Modifier,
    volume: Int,
    volumeUnit: String,
    onClick: (volume: Int) -> Unit
) {
    ElevatedButton (
        modifier = modifier.size(80.dp),
        contentPadding = PaddingValues(0.dp),
        onClick = { onClick(volume) },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_water),
                contentDescription = null
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = "$volume $volumeUnit",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun AddWaterButton(
    modifier: Modifier = Modifier,
    onAddWaterClick: () -> Unit
) {
    ElevatedButton(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        onClick = onAddWaterClick,
    ) {
        Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_add), contentDescription = null)

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.small_spacing)))

        Text(
            text = stringResource(R.string.add_water),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview
@Composable
private fun WaterVolumePreview() {
    AquaUpTheme {
        WaterVolumeButton(volume = 200, volumeUnit = "ml") { }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeContentPreview() {
    AquaUpTheme {
        Scaffold { innerPadding ->
            val state = HomeScreenState.Success.preview

            HomeContent(
                modifier = Modifier.padding(innerPadding),
                waterGoal = state.waterGoal,
                waterAmount = state.waterAmount,
                volumeUnit = state.volumeUnit,
                recentWaterVolumes = state.recentWaterVolumes,
                onEvent = {  }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeLoadingPreview() {
    AquaUpTheme {
        Scaffold { innerPadding ->
            HomeLoading(modifier = Modifier.padding(innerPadding))
        }
    }
}