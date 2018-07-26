package org.coner.timer.input.mapper

import mu.KotlinLogging
import org.coner.timer.input.FinishTriggerElapsedTimeOnly
import java.math.MathContext

private val logger = KotlinLogging.logger {  }
class JACTimerInputMapper : TimerInputMapper<String, FinishTriggerElapsedTimeOnly> {
    val roundingMode = MathContext(6)

    override fun map(rawTimerInput: String): FinishTriggerElapsedTimeOnly {
        logger.info { "" }
        val lineReversedStripped = rawTimerInput.substring(1).reversed()
        val et = lineReversedStripped.toBigDecimal(roundingMode)
        return FinishTriggerElapsedTimeOnly(et)
    }

}