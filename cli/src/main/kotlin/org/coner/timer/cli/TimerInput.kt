package org.coner.timer.cli

import com.fazecast.jSerialComm.SerialPort
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.output.TermUi
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import org.coner.core.client.ApiClient
import org.coner.core.client.api.EventsApi
import org.coner.timer.Timer
import org.coner.timer.input.mapper.JACTimerInputMapper
import org.coner.timer.input.reader.InputStreamTimerInputReader
import org.coner.timer.input.reader.JSerialCommTimerInputReader
import org.coner.timer.input.reader.PureJavaCommTimerInputReader
import org.coner.timer.model.FinishTriggerElapsedTimeOnly
import org.coner.timer.output.ConerCoreRunOutputWriter
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
    val mapper: String by mapperOption()
    val rawInputLogFile: File by rawInputLogFileArgument()
    val mappedInputWriter: String by mappedInputWriterArgument()
    val conerCoreServiceUrl by option()
    val conerCoreEventId by option()

    override fun run() {
        val config = InputStreamTimerInputReader.Config(inputFile.inputStream())
        val reader = InputStreamTimerInputReader(config)
        val rawInputWriter = FileAppendingOutputWriter(rawInputLogFile)
        val mapper = when (mapper) {
            "jacircuits" -> JACTimerInputMapper()
            else -> throw IllegalStateException()
        }
        val mappedInputWriter = when(mappedInputWriter) {
            "println" -> PrintlnTimerOutputWriter<FinishTriggerElapsedTimeOnly>()
            "coner-core-run" -> ConerCoreRunOutputWriter(buildConerCoreEventsApi(conerCoreServiceUrl!!), conerCoreEventId!!)
            else -> throw IllegalStateException()
        }
        val timer = Timer(
                reader = reader,
                rawInputWriter = rawInputWriter,
                mapper = mapper,
                mappedInputWriter = mappedInputWriter
        )
        runTimer(timer)
    }
}

class TimerCommPort : CliktCommand(name = "comm-port") {
    override fun run() = Unit
}

class TimerCommPortInput : CliktCommand(name = "input") {
    val serialPortLibrary: String by serialPortLibraryOption()
    val portName: String by argument()
            .choice(*CommPortIdentifier.getPortIdentifiers().toList().map { it.name }.toTypedArray())
    val mapper: String by mapperOption()
    val rawInputLogFile: File by rawInputLogFileArgument()
    val mappedInputWriter: String by mappedInputWriterArgument()
    val conerCoreServiceUrl by option()
    val conerCoreEventId by option()

    override fun run() {
        val reader = when (serialPortLibrary) {
            "purejavacomm" -> PureJavaCommTimerInputReader(PureJavaCommTimerInputReader.Config(
                    appName = "coner-timer-cli",
                    port = portName
            ))
            "jserialcomm" -> JSerialCommTimerInputReader(JSerialCommTimerInputReader.Config(
                    port = portName
            ))
            else -> throw IllegalStateException()
        }
        val rawInputWriter = FileAppendingOutputWriter(rawInputLogFile)
        val mapper = when (mapper) {
            "jacircuits" -> JACTimerInputMapper()
            else -> throw IllegalStateException()
        }
        val mappedInputWriter = when(mappedInputWriter) {
            "println" -> PrintlnTimerOutputWriter<FinishTriggerElapsedTimeOnly>()
            "coner-core-run" -> ConerCoreRunOutputWriter(buildConerCoreEventsApi(conerCoreServiceUrl!!), conerCoreEventId!!)
            else -> throw IllegalStateException()
        }
        val timer = Timer(
                reader = reader,
                rawInputWriter = rawInputWriter,
                mapper = mapper,
                mappedInputWriter = mappedInputWriter
        )
        runTimer(timer)
    }
}

class TimerCommPortList : CliktCommand(name = "list") {
    val serialPortLibrary: String by serialPortLibraryOption()

    override fun run() {
        when (serialPortLibrary) {
            "purejavacomm" -> usePureJavaComm()
            "jserialcomm" -> useJSerialComm()
        }
    }

    private fun usePureJavaComm() {
        val ports = CommPortIdentifier.getPortIdentifiers().toList().sortedBy(CommPortIdentifier::getName)
        for (port in ports) {
            TermUi.echo("Comm port: { name: ${port.name}  }")
        }
    }

    private fun useJSerialComm() {
        val ports = SerialPort.getCommPorts()
        for (port in ports) {
            TermUi.echo("Comm port: { systemPortName: ${port.systemPortName}, descriptivePortName: ${port.descriptivePortName}, portDescription: ${port.portDescription}}")
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

private fun buildConerCoreEventsApi(conerCoreServiceUrl: String): EventsApi {
    val apiClient = ApiClient()
    apiClient.basePath = conerCoreServiceUrl
    return EventsApi(apiClient)
}

private fun <RTI, I> runTimer(timer: Timer<RTI, I>) {
    TermUi.echo("Starting timer... ", trailingNewline = false)
    timer.start()
    TermUi.echo("OK")
    TermUi.echo("Press Enter to stop...")
    TermUi.confirm("", abort = false, promptSuffix = "", showDefault = false, default = true)
    TermUi.echo("Stopping timer...", trailingNewline = false)
    timer.stop()
    TermUi.echo("OK")
}

private fun CliktCommand.rawInputLogFileArgument() = argument().file(
        exists = true,
        folderOkay = false,
        writable = true
)

private fun CliktCommand.mapperOption() = option()
        .choice("jacircuits")
        .default("jacircuits")

private fun CliktCommand.mappedInputWriterArgument() = argument().choice("println", "coner-core-run")

private fun CliktCommand.serialPortLibraryOption() = option("--serial-port-library", "-l")
            .choice("purejavacomm", "jserialcomm")
            .default("purejavacomm")