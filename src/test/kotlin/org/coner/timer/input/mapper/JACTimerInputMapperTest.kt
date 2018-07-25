package org.coner.timer.input.mapper

import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class JACTimerInputMapperTest {

    val prefix = "\uFFFD"

    lateinit var reader: JACTimerInputMapper

    @Before
    fun before() {
        reader = JACTimerInputMapper()
    }

    @Test
    fun itShouldReadBasicInput() {
        val rawTimerInput = "${prefix}654.321"
        val expected = FinishTriggerElapsedTimeOnly(BigDecimal.valueOf(123456, 3))

        val actual = reader.map(rawTimerInput)

        assertEquals(expected, actual)
    }
}