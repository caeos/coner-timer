package org.coner.timer.input.mapper

import org.coner.timer.model.FinishTriggerElapsedTimeOnly
import java.math.BigDecimal
import java.math.MathContext

class JACTimerInputMapper : TimerInputMapper<String, FinishTriggerElapsedTimeOnly> {
    val roundingMode = MathContext(6)

    override fun map(rawTimerInput: String): FinishTriggerElapsedTimeOnly {
        val lineReversedStripped = rawTimerInput.substring(1).reversed()
        if (lineReversedStripped.length != 6) {
            throw MappingException("Invalid JAC timer input: $rawTimerInput")
        }
        val et = BigDecimal.valueOf(lineReversedStripped.toLong(), 3)
        return FinishTriggerElapsedTimeOnly(et)
    }

}