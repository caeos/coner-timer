package org.coner.timer.input.reader

interface TimerInputReader<RTI> {

    fun onStart()
    fun read(): RTI?
    fun onStop()
}
