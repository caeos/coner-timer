package org.coner.timer.input.mapper

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown
import org.coner.timer.model.FinishTriggerElapsedTimeOnly
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
        val rawTimerInput = "${prefix}654321"
        val expected = FinishTriggerElapsedTimeOnly(BigDecimal.valueOf(123456, 3))

        val actual = reader.map(rawTimerInput)

        assertEquals(expected, actual)
    }

    @Test
    fun itShouldThrowWithWrongLengthInput() {
        val rawTimerInput = "50"

        try {
            reader.map(rawTimerInput)
            failBecauseExceptionWasNotThrown(MappingException::class.java)
        } catch (t: Throwable) {
            assertThat(t).isInstanceOf(MappingException::class.java)
        }
    }
}