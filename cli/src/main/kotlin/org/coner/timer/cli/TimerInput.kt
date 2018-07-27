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
import org.coner.timer.input.reader.PureJavaCommTimerInputReader
import org.coner.timer.output.FileAppendingOutputWriter
import org.coner.timer.output.PrintlnTimerOutputWriter
import purejavacomm.CommPortIdentifier
import java.io.File

class Timer : CliktCommand() {
    override fun run() = Unit
}

class TimerFile : CliktCommand(name = "file") {
    override fun run() = Unit
}

class TimerFileInput : CliktCommand(name = "input") {
    val inputFile: File by argument().file(exists = true, readable = true, folderOkay = false)
    val mapper: String by argument().choice("jacircuits")
    val rawInputLogFile: File by argument().file(exists = true, folderOkay = false, writable = true)

    override fun run() {
        val config = InputStreamTimerInputReader.Config(inputFile.inputStream())
        val reader = InputStreamTimerInputReader(config)
        val mapper = when (mapper) {
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
        runTimer(timer)
    }
}

class TimerCommPort : CliktCommand(name = "comm-port") {
    override fun run() = Unit
}

class TimerCommPortInput : CliktCommand(name = "input") {
    val portName: String by argument()
            .choice(*CommPortIdentifier.getPortIdentifiers().toList().map { it.name }.toTypedArray())
    val mapper: String by argument().choice("jacircuits")
    val rawInputLogFile: File by argument().file(exists = true, folderOkay = false, writable = true)

    override fun run() {
        val config = PureJavaCommTimerInputReader.Config(
                appName = "coner-timer-cli",
                port = portName
        )
        val reader = PureJavaCommTimerInputReader(config)
        val rawInputWriter = FileAppendingOutputWriter(rawInputLogFile)
        val mapper = when (mapper) {
            "jacircuits" -> JACTimerInputMapper()
            else -> throw IllegalStateException()
        }
        val timer = Timer(
                reader = reader,
                rawInputWriter = rawInputWriter,
                mapper = mapper,
                mappedInputWriter = PrintlnTimerOutputWriter()
        )
        runTimer(timer)
    }
}

class TimerCommPortList : CliktCommand(name = "list") {
    override fun run() {
        val ports = CommPortIdentifier.getPortIdentifiers().toList().sortedBy(CommPortIdentifier::getName)
        for (port in ports) {
            TermUi.echo("Comm port: { name: ${port.name}  }")
        }
    }
}

fun main(args: Array<String>) = Timer()
        .subcommands(
                TimerFile()
                        .subcommands(TimerFileInput()),
                TimerCommPort()
                        .subcommands(TimerCommPortList(), TimerCommPortInput())
        )
        .main(args)

inline fun <RTI, I> runTimer(timer: Timer<RTI, I>) {
    TermUi.echo("Starting timer... ", trailingNewline = false)
    timer.start()
    TermUi.echo("OK")
    TermUi.echo("Press Enter to stop...")
    TermUi.confirm("", abort = false, promptSuffix = "", showDefault = false, default = true)
    TermUi.echo("Stopping timer...", trailingNewline = false)
    timer.stop()
    TermUi.echo("OK")
}