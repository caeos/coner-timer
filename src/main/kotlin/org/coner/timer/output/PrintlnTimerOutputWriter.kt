package org.coner.timer.output

class PrintlnTimerOutputWriter : TimerOutputWriter<Any> {
    override fun write(input: Any) {
        println(input)
    }
}