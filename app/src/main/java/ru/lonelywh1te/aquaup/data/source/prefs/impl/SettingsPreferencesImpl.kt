package ru.lonelywh1te.aquaup.data.source.prefs.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import ru.lonelywh1te.aquaup.domain.storage.SettingsPreferences
import ru.lonelywh1te.aquaup.domain.model.VolumeUnit

class SettingsPreferencesImpl(
    private val context: Context,
): SettingsPreferences {
    val prefs: SharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    override var volumeUnit: VolumeUnit
        get() = VolumeUnit.valueOf(prefs.getString(VOLUME_UNIT_KEY, VolumeUnit.ML.name)!!)
        set(value) = prefs.edit {
            putString(VOLUME_UNIT_KEY, value.name)
        }


    override var waterGoal: Int
        get() = prefs.getInt(WATER_GOAL_KEY, 0)
        set(value) = prefs.edit {
            putInt(WATER_GOAL_KEY, value)
        }

    companion object {
        private const val NAME = "settings"
        private const val VOLUME_UNIT_KEY = "volume_unit"
        private const val WATER_GOAL_KEY = "water_goal"
    }
}