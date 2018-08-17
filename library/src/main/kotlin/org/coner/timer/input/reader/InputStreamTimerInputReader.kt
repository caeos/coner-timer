package org.coner.timer.input.reader

import java.io.BufferedReader
import java.io.InputStream

class InputStreamTimerInputReader(val inputStream: InputStream) : TimerInputReader<String> {

    private lateinit var buffer: BufferedReader

    override fun onStart() {
        buffer = inputStream.bufferedReader()
    }

    override fun read(): String? {
        return buffer.readLine()
    }

    override fun onStop() {
        buffer.close()
    }
}