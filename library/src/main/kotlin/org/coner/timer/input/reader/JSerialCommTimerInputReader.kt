package org.coner.timer.input.reader

import com.fazecast.jSerialComm.SerialPort
import org.coner.timer.util.JSerialCommWrapper
import java.io.BufferedReader
import java.io.IOException

class JSerialCommTimerInputReader(
        val jSerialComm: JSerialCommWrapper,
        val port: String
) : TimerInputReader<String> {

    private lateinit var commPort: SerialPort
    private lateinit var buffer: BufferedReader

    override fun onStart() {
        commPort = jSerialComm.getSerialPorts().first { it.systemPortName == port }

        // settings needed for RaceAmerica AC4 -- consider conversion to params
        commPort.baudRate = 9600
        commPort.numDataBits = 8
        commPort.numStopBits = SerialPort.ONE_STOP_BIT
        commPort.parity = SerialPort.NO_PARITY

        val success = commPort.openPort()
        require(success) { "Failed to open port: ${port}" }
        buffer = commPort.inputStream.bufferedReader()
    }

    override fun read(): String? {
        try {
            return buffer.readLine()
        } catch (e: IOException) {
            return null
        }
    }

    override fun onStop() {
        buffer.close()
        val success = commPort.closePort()
        require(success) { "Failed to close port: ${port}" }
    }
}