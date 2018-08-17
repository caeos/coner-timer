package org.coner.timer.input.reader

import com.fazecast.jSerialComm.SerialPort
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.coner.timer.util.JSerialCommWrapper
import org.junit.Ignore
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStream

class JSerialCommTimerInputReaderTest {

    lateinit var reader: JSerialCommTimerInputReader

    val portName = "port"
    lateinit var jSerialComm: JSerialCommWrapper
    lateinit var inputStream: InputStream
    lateinit var buffer: BufferedReader

    @Test
    @Ignore("TODO: rewrite this test without inputstream mockk mocks")
    fun itShouldStartReadAndStop() {
        jSerialComm = mockk(relaxed = true)
        val commPort: SerialPort = mockk(relaxed = true)
        every { jSerialComm.getSerialPorts() }.returns(arrayOf(commPort))
        every { commPort.systemPortName }.returns(portName)
        every { commPort.openPort() }.returns(true)
        buffer = mockk(relaxed = true)
        inputStream = mockk(relaxed = true)
        every { commPort.inputStream }.returns(inputStream)
        every { commPort.inputStream.bufferedReader() }.returns(buffer)
        every { buffer.readLine() }.returns("test")
        reader = JSerialCommTimerInputReader(jSerialComm, portName)

        reader.onStart()
        val actual = reader.read()
        reader.onStop()

        verify { buffer.readLine() }
        verify { buffer.close() }
        verify { commPort.closePort() }
        assertThat(actual).isEqualTo("test")
    }

}