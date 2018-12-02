package org.coner.timer.input.reader

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream

class InputStreamTimerInputReader(val inputStream: InputStream) : TimerInputReader<String> {

    private lateinit var buffer: BufferedReader

    override fun onStart() {
        buffer = inputStream.bufferedReader()
    }

    override fun read(): String? {
        return try {
            buffer.readLine()
        } catch (ioException: IOException) {
            null
        }
    }

    override fun onStop() {
        buffer.close()
    }
}