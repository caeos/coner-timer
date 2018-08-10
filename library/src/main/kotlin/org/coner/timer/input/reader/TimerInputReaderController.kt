package org.coner.timer.input.reader

import java.util.concurrent.atomic.AtomicBoolean

class TimerInputReaderController<RTI>(
        private val started: AtomicBoolean = AtomicBoolean(false),
        private val consumed: AtomicBoolean = AtomicBoolean(false),
        private val reader: TimerInputReader<RTI>
) {

    fun start() {
        synchronized(this) {
            require(!started.get()) { "Already started. Double entry not permitted." }
            require(!consumed.get()) { "Object consumed. Reuse not permitted." }
            reader.onStart()
            started.set(true)
        }
    }

    fun read(): RTI? {
        synchronized(this) {
            require(started.get()) { "Not started. Read is not a valid operation currently." }
            return reader.read()
        }
    }

    fun stop() {
        synchronized(this) {
            require(started.get()) { "Not started. Stop is not a valid operation." }
            require(!consumed.get()) { "Already consumed. Once is enough." }
            reader.onStop()
            consumed.set(true)
        }
    }
}