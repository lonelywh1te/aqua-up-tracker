package ru.lonelywh1te.aquaup.presentation.home

import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit


sealed class HomeScreenState {

    data class Success(
        val waterGoal: Int,
        val waterAmount: Int,
        val recentWaterVolumes: List<Int>,
        val volumeUnit: VolumeUnit,
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

    data class Error(val message: String): HomeScreenState()


}

