package org.coner.timer.input.reader

import java.util.concurrent.atomic.AtomicBoolean

abstract class TimerInputReader<C, RTI>(val config: C) {

    private var started = AtomicBoolean(false)
    private var consumed = AtomicBoolean(false)

    open protected val onStart: () -> Unit = {}
    open protected val onStop: () -> Unit = {}

    fun start() {
        synchronized(this) {
            require(!started.get()) { "Already started. Double entry not permitted." }
            require(!consumed.get()) { "Object consumed. Reuse not permitted." }
            onStart()
            started.set(true)
        }
    }

    abstract fun read(): RTI?

    fun stop() {
        synchronized(this) {
            require(started.get()) { "Not started. Stop is not a valid operation." }
            require(!consumed.get()) { "Already consumed. Once is enough." }
            onStop()
            consumed.set(true)
        }
    }
}
