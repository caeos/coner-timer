package org.coner.timer.input

interface TimerInputReader<T> {

    fun read(rawTimerInput: String): T
}