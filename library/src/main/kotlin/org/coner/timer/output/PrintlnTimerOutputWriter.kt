package org.coner.timer.output

import org.coner.timer.model.FinishTriggerElapsedTimeOnly

class PrintlnTimerOutputWriter<I> : TimerOutputWriter<I> {
    override fun write(input: I) {
        when (input) {
            is FinishTriggerElapsedTimeOnly -> println(input.et)
            else -> println(input)
        }
    }
}