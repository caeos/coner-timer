package org.coner.timer.input.mapper

import org.coner.timer.model.FinishTriggerElapsedTimeOnly
import java.math.MathContext

class JACTimerInputMapper : TimerInputMapper<String, FinishTriggerElapsedTimeOnly> {
    val roundingMode = MathContext(6)

    override fun map(rawTimerInput: String): FinishTriggerElapsedTimeOnly {
        val lineReversedStripped = rawTimerInput.substring(1).reversed()
        val et = lineReversedStripped.toBigDecimal(roundingMode)
        return FinishTriggerElapsedTimeOnly(et)
    }

}