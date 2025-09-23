package ru.lonelywh1te.aquaup.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.lonelywh1te.aquaup.domain.model.settings.convertMlToOz
import ru.lonelywh1te.aquaup.domain.model.settings.convertOzToMl

class VolumeUnitTest {

    @Test
    fun `convertMlToOz convert correctly`() {
        assertEquals(0, convertMlToOz(0))
        assertEquals(1, convertMlToOz(30))
        assertEquals(34, convertMlToOz(1000))
    }

    @Test
    fun `convertOzToMl convert correctly`() {
        assertEquals(0, convertOzToMl(0))
        assertEquals(30, convertOzToMl(1))
        assertEquals(2957, convertOzToMl(100))
    }

}