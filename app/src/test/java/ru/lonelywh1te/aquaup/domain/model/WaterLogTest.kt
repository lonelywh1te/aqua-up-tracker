package ru.lonelywh1te.aquaup.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import utils.WaterLogTestFactory

class WaterLogTest {

    @Nested
    inner class WaterLogConvertUnit {

        @Test
        fun `convert WaterLog from Oz to Ml correctly`() {
            val waterLog = WaterLogTestFactory.create(seed = 0, amountMl = 1)

            val expected = waterLog.copy(amountMl = 30)
            val actual = waterLog.convertUnit(VolumeUnit.Oz, VolumeUnit.Ml)

            assertEquals(expected, actual)
        }

        @Test
        fun `convert WaterLog from Ml to Oz correctly`() {
            val waterLog = WaterLogTestFactory.create(seed = 0, amountMl = 30)

            val expected = waterLog.copy(amountMl = 1)
            val actual = waterLog.convertUnit(VolumeUnit.Ml, VolumeUnit.Oz)

            assertEquals(expected, actual)
        }

        @Test
        fun `do nothing when units are Ml`() {
            val waterLog = WaterLogTestFactory.create(seed = 0, amountMl = 1)

            val expected = waterLog
            val actual = waterLog.convertUnit(VolumeUnit.Ml, VolumeUnit.Ml)

            assertEquals(expected, actual)
        }

        @Test
        fun `do nothing when units are Oz`() {
            val waterLog = WaterLogTestFactory.create(seed = 0, amountMl = 1)

            val expected = waterLog
            val actual = waterLog.convertUnit(VolumeUnit.Oz, VolumeUnit.Oz)

            assertEquals(expected, actual)
        }

    }

    @Nested
    inner class ListWaterLogConvertUnit {
        @Test
        fun `convert and return mapped list`() {
            val waterLog = WaterLogTestFactory.createList(seed = 0, count = 5).map { it.copy(amountMl = 30) }

            val expected = waterLog.map { it.copy(amountMl = 1) }
            val actual = waterLog.convertUnit(VolumeUnit.Ml, VolumeUnit.Oz)

            assertEquals(expected, actual)
        }
    }


}