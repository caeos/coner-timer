package org.coner.timer

import org.coner.timer.input.mapper.TimerInputMapper
import org.coner.timer.input.reader.TimerInputReader
import org.coner.timer.output.TimerOutputWriter
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

class Timer<RTI, I>(
        val reader: TimerInputReader<*, RTI>,
        val rawInputWriter: TimerOutputWriter<RTI>? = null,
        val mapper: TimerInputMapper<RTI, out I>,
        val mappedInputWriter: TimerOutputWriter<I>
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
            rawInputWriter?.write(rawTimerInput)
            val mappedInput = mapper.map(rawTimerInput)
            if (mappedInput == null) {
                Thread.sleep(100)
                continue
            }
            mappedInputWriter.write(mappedInput)
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