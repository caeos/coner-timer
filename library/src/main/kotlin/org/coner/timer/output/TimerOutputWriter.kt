package org.coner.timer.output

interface TimerOutputWriter<I> {

    fun write(input: I)
}