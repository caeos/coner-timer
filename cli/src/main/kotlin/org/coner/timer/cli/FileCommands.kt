package org.coner.timer.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import org.coner.timer.Timer
import org.coner.timer.input.mapper.JACTimerInputMapper
import org.coner.timer.input.reader.InputStreamTimerInputReader
import org.coner.timer.input.reader.TimerInputReaderController
import java.io.File

class FileCommand : CliktCommand(name = "file") {
    override fun run() = Unit
}

class FileReplayCommand : CliktCommand(
        name = "replay",
        help = "Replay the contents of a raw input log file"
) {
    private val source: File by argument()
            .file(
                    mustExist = true,
                    canBeDir = false,
                    mustBeReadable = true
            )
    private val watch: Boolean by option(
            names = *arrayOf("--watch"),
            help = "When present, replay will continue to watch for times appended to the end of the file. When absent, replay will stop and exit after encountering an empty line or end of file."
    )
            .flag(default = false)
    private val verbose: Boolean by option(
            names = *arrayOf("--verbose"),
            help = "Verbosely print details."
    )
            .flag(default = false)

    override fun run() {
        val reader = InputStreamTimerInputReader(source.inputStream())
        val controller = TimerInputReaderController(reader = reader)
        val mapper = JACTimerInputMapper()
        val mappedInputWriter = SimpleEchoMappedInputWriter()
        val timer = Timer(
                controller = controller,
                mapper = mapper,
                mappedInputWriter = mappedInputWriter,
                continueOnNull = watch
        )
        runTimer(timer, confirmToStop = watch, verbose = verbose)
    }
}
