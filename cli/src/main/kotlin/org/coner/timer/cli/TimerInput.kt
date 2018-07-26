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
import org.coner.timer.output.PrintlnTimerOutputWriter
import java.io.File

class TimerInput : CliktCommand() {
    override fun run() = Unit
}

class File : CliktCommand() {
    val file: File by argument().file(exists = true, readable = true, folderOkay = false)
    val mapper: String by argument().choice("jacircuits")

    override fun run() {
        val config = InputStreamTimerInputReader.Config(file.inputStream())
        val reader = InputStreamTimerInputReader(config)
        val mapper = when(mapper) {
            "jacircuits" -> JACTimerInputMapper()
            else -> throw IllegalStateException()
        }
        val writer = PrintlnTimerOutputWriter()
        val timer = Timer(reader, mapper, writer)
        TermUi.confirm("Start timer?", abort = true)
        timer.start()
        TermUi.confirm("Stop timer?")
        timer.stop()
    }
}

fun main(args: Array<String>) = TimerInput()
        .subcommands(File())
        .main(args)