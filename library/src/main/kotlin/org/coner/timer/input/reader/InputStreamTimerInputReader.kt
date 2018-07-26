package org.coner.timer.input.reader

import java.io.BufferedReader
import java.io.InputStream

class InputStreamTimerInputReader(config: Config) : TimerInputReader<InputStreamTimerInputReader.Config, String>(config) {

    private lateinit var buffer: BufferedReader

    override val onStart = {
        buffer = config.inputStream.bufferedReader()
    }

    override fun read(): String? {
        return buffer.readLine()
    }

    override val onStop = {
        buffer.close()
    }

    data class Config(val inputStream: InputStream)
}