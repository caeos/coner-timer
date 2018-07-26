package org.coner.timer.input.mapper

interface TimerInputMapper<RTI, I> {

    fun map(rawTimerInput: RTI): I?
}