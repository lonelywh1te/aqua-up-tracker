package ru.lonelywh1te.aquaup.domain.storage

import ru.lonelywh1te.aquaup.domain.model.VolumeUnit

interface SettingsPreferences {

    var volumeUnit: VolumeUnit

    var waterGoal: Int

}