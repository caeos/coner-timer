package org.coner.timer.input.reader

import com.fazecast.jSerialComm.SerialPort
import java.io.BufferedReader

class JSerialCommTimerInputReader(config: Config) : TimerInputReader<JSerialCommTimerInputReader.Config, String>(config) {

    private lateinit var commPort: SerialPort
    private lateinit var buffer: BufferedReader

    override val onStart = {
        commPort = SerialPort.getCommPorts().first { it.systemPortName == config.port }
        val success = commPort.openPort()
        require(success) { "Failed to open port: ${config.port}"}
        buffer = commPort.inputStream.bufferedReader()
    }

    override fun read() = buffer.readLine()

    override val onStop = {
        val success = commPort.closePort()
        require(success) { "Failed to close port: ${config.port}"}
    }

    data class Config(val port: String)
}