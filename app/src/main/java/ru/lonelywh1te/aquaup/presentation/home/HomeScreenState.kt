package ru.lonelywh1te.aquaup.presentation.home

import androidx.annotation.StringRes
import okio.IOException
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.result.AppError
import ru.lonelywh1te.aquaup.presentation.ui.utils.asStringRes
import kotlin.math.roundToInt


sealed class HomeScreenState {

    data class Success(
        val waterGoal: Int,
        val waterAmount: Int,
        val recentWaterVolumes: List<Int>,
        val volumeUnit: VolumeUnit,
        val progressPercentage: Int = if (waterGoal != 0) ((waterAmount.toFloat() / waterGoal) * 100).roundToInt() else 0
    ): HomeScreenState() {
        companion object {
            val preview = Success(
                waterGoal = 1800,
                waterAmount = 1000,
                recentWaterVolumes = listOf(100, 200, 300, 400, 500),
                volumeUnit = VolumeUnit.Ml,
            )
        }
    }

    data object Loading: HomeScreenState()

    data class Error(
        @StringRes val messageStringRes: Int,
        val exceptionDetails: String? = null
    ): HomeScreenState() {
        companion object {
            val preview = HomeScreenState.Error(
                messageStringRes = AppError.Unknown(IOException()).asStringRes(),
                exceptionDetails = IOException().message
            )
        }
    }


}

