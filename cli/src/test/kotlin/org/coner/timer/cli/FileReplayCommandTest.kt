package org.coner.timer.cli

import assertk.all
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.messageContains
import com.github.ajalt.clikt.core.BadParameterValue
import com.github.ajalt.clikt.core.CliktCommand
import org.awaitility.Awaitility
import org.junit.jupiter.api.*
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.*

class FileReplayCommandTest {

    lateinit var app: CliktCommand

    private lateinit var output: ByteArrayOutputStream
    private val originalSystemOut = System.out

    @BeforeEach
    fun beforeEach() {
        app = factory()
        output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
    }

    @AfterEach
    fun afterAll() {
        System.setOut(originalSystemOut)
    }

    @Test
    fun `It should replay log file`() {
        assertDoesNotThrow {
            app.parse(listOf(
                    "file",
                    "replay",
                    "src/test/resources/org/coner/timer/cli/raw-input.log"
            ))
            Awaitility.await().until { output.toString().lines().size >= 4 }
        }

        val actual = output.toString().trim().lines()

        assertThat(actual, "output").containsExactly(
                "123.456",
                "654.321",
                "100.000",
                "0.001"
        )
    }

    @Test
    fun `It should throw when file does not exist`() {
        val fileNotExist = "does-not-exist-${UUID.randomUUID()}"

        val actual = assertThrows<BadParameterValue> {
            app.parse(listOf(
                    "file",
                    "replay",
                    fileNotExist
            ))
        }

        assertThat(actual).all {
            messageContains("SOURCE")
            messageContains(fileNotExist)
        }
    }
}