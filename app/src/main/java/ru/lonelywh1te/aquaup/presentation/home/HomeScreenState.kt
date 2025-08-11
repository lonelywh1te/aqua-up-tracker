package ru.lonelywh1te.aquaup.presentation.home

import ru.lonelywh1te.aquaup.domain.model.VolumeUnit


sealed class HomeScreenState {

    data class Success(
        val waterGoal: Int,
        val waterAmount: Int,
        val recentWaterVolumes: List<Int>,
        val volumeUnit: VolumeUnit,
    ): HomeScreenState() {
        companion object {
            fun getPreviewState(): Success {
                return Success(
                    waterGoal = 1800,
                    waterAmount = 1000,
                    recentWaterVolumes = listOf(100, 200, 300, 400, 500),
                    volumeUnit = VolumeUnit.ML,
                )
            }
        }
    }

    data object Loading: HomeScreenState()

    data class Error(val message: String): HomeScreenState()


}

