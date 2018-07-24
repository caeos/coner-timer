package org.coner.timer.input

import java.math.MathContext

class JACTimerInputReader : TimerInputReader<FinishTriggerElapsedTimeOnly> {
    val roundingMode = MathContext(6)

    override fun read(rawTimerInput: String): FinishTriggerElapsedTimeOnly {
        val lineReversedStripped = rawTimerInput.substring(1).reversed()
        val et = lineReversedStripped.toBigDecimal(roundingMode)
        return FinishTriggerElapsedTimeOnly(et)
    }

}