package org.coner.timer.input.reader

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown
import org.coner.timer.util.PureJavaCommWrapper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import purejavacomm.CommPort
import purejavacomm.CommPortIdentifier
import java.io.File
import java.io.IOException

class PureJavaCommTimerInputReaderTest {

    private lateinit var reader: PureJavaCommTimerInputReader

    private val appName = "coner-timer-library:test"
    private val port = "port"

    @MockK(relaxUnitFun = true)
    private lateinit var pureJavaComm: PureJavaCommWrapper
    @MockK(relaxUnitFun = true)
    private lateinit var commPort: CommPort
    @MockK(relaxUnitFun = true)
    private lateinit var commPortIdentifier: CommPortIdentifier

    @Rule
    @JvmField
    val folder = TemporaryFolder()
    private lateinit var file: File

    @Before
    fun before() {
        file = folder.newFile()
        MockKAnnotations.init(this)
        every { pureJavaComm.getCommPortIdentifier(port) }.returns(commPortIdentifier)
        every { commPortIdentifier.open(appName, any()) }.returns(commPort)
        every { commPort.inputStream }.returns(file.inputStream())
        reader = PureJavaCommTimerInputReader(pureJavaComm, appName, port)
    }

    @After
    fun after() {

    }

    @Test
    fun itShouldStartWhenPortOpensSuccessfully() {
        reader.onStart()

        verify { pureJavaComm.getCommPortIdentifier(port) }
        verify { commPortIdentifier.open(appName, any()) }
    }

    @Test
    fun itShouldRead() {
        reader.onStart()
        val input = "test"
        file.writeText(input)

        val actual = reader.read()

        assertThat(input).isEqualTo(actual)
    }

    @Test
    fun itShouldStop() {
        reader.onStart()

        reader.onStop()

        verify { commPort.close() }
        try {
            reader.read()
            failBecauseExceptionWasNotThrown(IOException::class.java)
        } catch (t: Throwable) {
            assertThat(t).isInstanceOf(IOException::class.java)
        }
    }

}