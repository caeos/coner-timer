package org.coner.timer.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.file
import org.coner.timer.Timer
import org.coner.timer.input.mapper.JACTimerInputMapper
import org.coner.timer.input.reader.InputStreamTimerInputReader
import org.coner.timer.input.reader.TimerInputReaderController
import java.io.File

class FileCommand : CliktCommand(name = "file") {
    override fun run() = Unit
}

class FileReplayCommand : CliktCommand(name = "replay") {
    private val source: File by argument().file(mustExist = true, canBeDir = false, mustBeReadable = true)

    override fun run() {
        val reader = InputStreamTimerInputReader(source.inputStream())
        val controller = TimerInputReaderController(reader = reader)
        val mapper = JACTimerInputMapper()
        val mappedInputWriter = SimpleEchoMappedInputWriter()
        val timer = Timer(
                controller = controller,
                mapper = mapper,
                mappedInputWriter = mappedInputWriter
        )
        runTimer(timer)
    }
}