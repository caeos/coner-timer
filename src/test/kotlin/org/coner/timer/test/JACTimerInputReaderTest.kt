package org.coner.timer.test

import org.coner.timer.input.FinishTriggerElapsedTimeOnly
import org.coner.timer.input.JACTimerInputReader
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class JACTimerInputReaderTest {

    val prefix = "\uFFFD"

    lateinit var reader: JACTimerInputReader

    @Before
    fun before() {
        reader = JACTimerInputReader()
    }

    @Test
    fun itShouldReadBasicInput() {
        val rawTimerInput = "${prefix}654.321"
        val expected = FinishTriggerElapsedTimeOnly(BigDecimal.valueOf(123456, 3))

        val actual = reader.read(rawTimerInput)

        assertEquals(expected, actual)
    }
}