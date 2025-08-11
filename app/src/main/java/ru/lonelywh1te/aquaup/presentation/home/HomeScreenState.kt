package ru.lonelywh1te.aquaup.presentation.home

import ru.lonelywh1te.aquaup.domain.VolumeUnit

data class HomeScreenState(
    val waterGoal: Int,
    val waterAmount: Int,
    val waterVolumes: List<Int>,
    val volumeUnit: VolumeUnit,
) {
    companion object {
        fun getPreviewState(): HomeScreenState {
            return HomeScreenState(
                waterGoal = 1800,
                waterAmount = 1000,
                waterVolumes = listOf(100, 200, 300, 400, 500),
                volumeUnit = VolumeUnit.ML,
            )
        }
    }
}
