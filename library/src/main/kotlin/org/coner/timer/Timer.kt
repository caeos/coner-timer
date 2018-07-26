package org.coner.timer

import org.coner.timer.input.mapper.TimerInputMapper
import org.coner.timer.input.reader.TimerInputReader
import org.coner.timer.output.TimerOutputWriter
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

class Timer<RTI, I>(
        val reader: TimerInputReader<*, RTI>,
        val mapper: TimerInputMapper<RTI, out I>,
        val writer: TimerOutputWriter<I>
) {

    private val started = AtomicBoolean(false)
    private var loop: Thread? = null

    fun start() {
        synchronized(this) {
            require(!started.get()) { "This timer is already started" }
            started.set(true)
            reader.start()
            loop = thread(name = "Timer") { loop() }
        }
    }

    private fun loop() {
        while (started.get()) {
            val rawTimerInput = reader.read()
            if (rawTimerInput == null) {
                Thread.sleep(100)
                continue
            }
            val input = mapper.map(rawTimerInput)
            if (input == null) {
                Thread.sleep(100)
                continue
            }
            writer.write(input)
        }
    }

    fun stop() {
        synchronized(this) {
            require(started.getAndSet(false)) { "This timer is already stopped"}
            reader.stop()
            loop = null
        }
    }
}