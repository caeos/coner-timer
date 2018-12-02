package org.coner.timer.input.reader

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeUnit

class InputStreamTimerInputReader(
        private val inputStream: InputStream,
        private val receiveTimeout: Long = 100
) : TimerInputReader<String> {

    private lateinit var buffer: BufferedReader

    override fun onStart() {
        buffer = inputStream.bufferedReader()
    }

    override fun read(): String? {
        val line = try {
            buffer.readLine()
        } catch (ioException: IOException) {
            null
        }
        return if (line?.isNotEmpty() == true) {
            line
        } else {
            Thread.sleep(receiveTimeout)
            null
        }
    }

    override fun onStop() {
        buffer.close()
    }
}