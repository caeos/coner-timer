package org.coner.timer.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.output.TermUi
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import org.coner.timer.Timer
import org.coner.timer.input.mapper.JACTimerInputMapper
import org.coner.timer.input.reader.InputStreamTimerInputReader
import org.coner.timer.output.FileAppendingOutputWriter
import org.coner.timer.output.PrintlnTimerOutputWriter
import java.io.File

class TimerInput : CliktCommand() {
    override fun run() = Unit
}

class File : CliktCommand() {
    val inputFile: File by argument().file(exists = true, readable = true, folderOkay = false)
    val mapper: String by argument().choice("jacircuits")
    val rawInputLogFile: File by argument().file(exists = true, folderOkay = false, writable = true)

    override fun run() {
        val config = InputStreamTimerInputReader.Config(inputFile.inputStream())
        val reader = InputStreamTimerInputReader(config)
        val mapper = when(mapper) {
            "jacircuits" -> JACTimerInputMapper()
            else -> throw IllegalStateException()
        }
        val rawInputWriter = FileAppendingOutputWriter(rawInputLogFile)
        val timer = Timer(
                reader = reader,
                rawInputWriter = rawInputWriter,
                mapper = mapper,
                mappedInputWriter = PrintlnTimerOutputWriter()
        )
        TermUi.echo("Starting timer... ", trailingNewline = false)
        timer.start()
        TermUi.echo("OK")
        TermUi.echo("Press Enter to stop...")
        TermUi.confirm("", abort = false, promptSuffix = "", showDefault = false, default = true)
        TermUi.echo("Stopping timer...", trailingNewline = false)
        timer.stop()
        TermUi.echo("OK")
    }
}

fun main(args: Array<String>) = TimerInput()
        .subcommands(File())
        .main(args)