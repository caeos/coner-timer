package org.coner.timer.input.reader

import org.coner.timer.util.PureJavaCommWrapper
import purejavacomm.CommPort
import java.io.BufferedReader
import java.io.IOException

class PureJavaCommTimerInputReader(
        val pureJavaComm: PureJavaCommWrapper,
        val appName: String,
        val port: String,
        private val connectTimeout: Int = 500,
        private val receiveTimeout: Int = 100
) : TimerInputReader<String> {

    private lateinit var commPort: CommPort
    private lateinit var buffer: BufferedReader

    override fun onStart() {
        val identifier = pureJavaComm.getCommPortIdentifier(port)
        commPort = identifier.open(appName, connectTimeout)
        commPort.enableReceiveTimeout(receiveTimeout)
        buffer = commPort.inputStream.bufferedReader()
    }

    override fun read() : String? {
        return try {
            buffer.readLine()
        } catch (ioException: IOException) {
            null
        }
    }

    override fun onStop() {
        buffer.close()
        commPort.close()
    }
}
