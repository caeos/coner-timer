package org.coner.timer.input.reader

import purejavacomm.CommPort
import purejavacomm.CommPortIdentifier
import java.io.BufferedReader

class PureJavaCommTimerInputReader(config: Config) : TimerInputReader<PureJavaCommTimerInputReader.Config, String>(config) {

    private lateinit var commPort: CommPort
    private lateinit var buffer: BufferedReader

    override val onStart = {
        val identifier = CommPortIdentifier.getPortIdentifier(config.port)
        commPort = identifier.open(config.appName, config.timeout)
        buffer = commPort.inputStream.bufferedReader()
    }

    override fun read() = buffer.readLine()

    override val onStop = {
        buffer.close()
        commPort.close()
    }

    data class Config(val appName: String, val port: String, val timeout: Int = 10000)
}