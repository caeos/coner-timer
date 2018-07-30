package org.coner.timer.output

class PrintlnTimerOutputWriter<I> : TimerOutputWriter<I> {
    override fun write(input: I) {
        println(input)
    }
}