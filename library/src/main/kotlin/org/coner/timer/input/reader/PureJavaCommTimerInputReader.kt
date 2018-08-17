package org.coner.timer.input.reader

import org.coner.timer.util.PureJavaCommWrapper
import purejavacomm.CommPort
import java.io.BufferedReader

class PureJavaCommTimerInputReader(
        val pureJavaComm: PureJavaCommWrapper,
        val appName: String,
        val port: String,
        val timeout: Int = 10000
) : TimerInputReader<String> {

    private lateinit var commPort: CommPort
    private lateinit var buffer: BufferedReader

    override fun onStart() {
        val identifier = pureJavaComm.getCommPortIdentifier(port)
        commPort = identifier.open(appName, timeout)
        buffer = commPort.inputStream.bufferedReader()
    }

    override fun read() = buffer.readLine()

    override fun onStop() {
        buffer.close()
        commPort.close()
    }

}