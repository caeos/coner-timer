package org.coner.timer.output

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class FileAppendingOutputWriterTest {

    lateinit var writer: FileAppendingOutputWriter

    lateinit var file: File

    @Rule @JvmField
    val folder = TemporaryFolder()

    @Before
    fun before() {
        file = folder.newFile()
    }

    @Test
    fun itShouldAppendToFileWithLineSeparator() {
        writer = FileAppendingOutputWriter(file)
        val input = "test"

        writer.write(input)

        assertThat(file).hasContent("test" + System.lineSeparator())
    }

    @Test
    fun itShouldAppendToFileWithoutLineSeparator() {

        writer = FileAppendingOutputWriter(file, appendLineSeparator = false)
        val input = "test"

        writer.write(input)

        assertThat(file).hasContent("test")
    }

}