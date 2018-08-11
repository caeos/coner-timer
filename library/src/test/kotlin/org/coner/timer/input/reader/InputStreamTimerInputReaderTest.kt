package org.coner.timer.input.reader

import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.InputStream

class InputStreamTimerInputReaderTest {

    lateinit var reader: InputStreamTimerInputReader

    lateinit var inputStream: InputStream
    lateinit var file: File

    @Rule @JvmField
    val folder = TemporaryFolder()

    @Before
    fun before() {
        file = folder.newFile()

        reader = InputStreamTimerInputReader(file.inputStream())
        reader.onStart()
    }

    @Test
    fun itShouldRead() {
        file.writeText("foo")

        val actual = reader.read()

        assertThat(actual).isEqualTo("foo")
    }

    @After
    fun after() {
        reader.onStop()
    }
}