package org.coner.timer.input.reader

import purejavacomm.CommPort
import purejavacomm.CommPortIdentifier
import java.io.BufferedReader

class PureJavaCommTimerInputReader(
        val appName: String,
        val port: String,
        val timeout: Int = 10000
) : TimerInputReader<String> {

    private lateinit var commPort: CommPort
    private lateinit var buffer: BufferedReader

    override fun onStart() {
        val identifier = CommPortIdentifier.getPortIdentifier(port)
        commPort = identifier.open(appName, timeout)
        buffer = commPort.inputStream.bufferedReader()
    }

    override fun read() = buffer.readLine()

    override fun onStop() {
        buffer.close()
        commPort.close()
    }

}