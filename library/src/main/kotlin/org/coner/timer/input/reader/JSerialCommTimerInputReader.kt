package org.coner.timer.input.reader

import com.fazecast.jSerialComm.SerialPort
import org.coner.timer.util.JSerialCommWrapper
import java.io.BufferedReader

class JSerialCommTimerInputReader(
        val jSerialComm: JSerialCommWrapper,
        val port: String
) : TimerInputReader<String> {

    private lateinit var commPort: SerialPort
    private lateinit var buffer: BufferedReader

    override fun onStart() {
        commPort = jSerialComm.getSerialPorts().first { it.systemPortName == port }
        val success = commPort.openPort()
        require(success) { "Failed to open port: ${port}"}
        buffer = commPort.inputStream.bufferedReader()
    }

    override fun read() = buffer.readLine()

    override fun onStop() {
        buffer.close()
        val success = commPort.closePort()
        require(success) { "Failed to close port: ${port}"}
    }
}