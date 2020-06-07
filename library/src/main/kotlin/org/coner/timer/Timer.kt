package org.coner.timer

import org.coner.timer.input.mapper.TimerInputMapper
import org.coner.timer.input.reader.TimerInputReaderController
import org.coner.timer.output.TimerOutputWriter
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

class Timer<RTI, I>(
        val controller: TimerInputReaderController<RTI>,
        val rawInputWriter: TimerOutputWriter<RTI>? = null,
        val mapper: TimerInputMapper<RTI, out I>,
        val mappedInputWriter: TimerOutputWriter<I>,
        private val continueOnNull: Boolean = true
) {

    private val started = AtomicBoolean(false)
    private var loop: Thread? = null
    val looping: Boolean get() = started.get()

    fun start() {
        synchronized(this) {
            require(!started.get()) { "This timer is already started" }
            started.set(true)
            controller.start()
            loop = thread(name = "Timer") { loop() }
        }
    }

    private fun loop() {
        while (started.get()) {
            try {
                val rawTimerInput = controller.read()
                        ?: if (!continueOnNull) {
                            stop()
                            return
                        } else {
                            continue
                        }
                rawInputWriter?.write(rawTimerInput)
                val mappedInput = mapper.map(rawTimerInput) ?: continue
                mappedInputWriter.write(mappedInput)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    fun stop() {
        synchronized(this) {
            require(started.getAndSet(false)) { "This timer is already stopped"}
            controller.stop()
            loop = null
        }
    }
}