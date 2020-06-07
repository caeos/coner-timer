package org.coner.timer.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.choice
import org.coner.timer.Timer
import org.coner.timer.input.mapper.JACTimerInputMapper
import org.coner.timer.input.reader.PureJavaCommTimerInputReader
import org.coner.timer.input.reader.TimerInputReaderController
import org.coner.timer.output.FileAppendingOutputWriter
import org.coner.timer.util.PureJavaCommWrapper
import purejavacomm.CommPortIdentifier
import java.io.File


class PortCommand : CliktCommand(name = "port") {
    override fun run() = Unit
}

class PortListCommand : CliktCommand(name = "list") {

    override fun run() {
        listPortNames().forEach { portName ->
            echo(portName)
        }
    }
}

class PortCaptureCommand : CliktCommand(name = "capture") {
    private val portName: String by argument()
            .choice(*listPortNames())
    val rawLog: File? by rawInputLogFileArgument()

    override fun run() {
        val reader = PureJavaCommTimerInputReader(
                pureJavaComm = PureJavaCommWrapper(),
                appName = "coner-timer-cli",
                port = portName
        )
        val readerController = TimerInputReaderController(reader = reader)
        val rawInputWriter = rawLog?.let { FileAppendingOutputWriter(it) }
        val mapper = JACTimerInputMapper()
        val mappedInputWriter = SimpleEchoMappedInputWriter()
        val timer = Timer(
                controller = readerController,
                rawInputWriter = rawInputWriter,
                mapper = mapper,
                mappedInputWriter = mappedInputWriter
        )
        runTimer(timer)
    }
}


private fun listPortNames() = CommPortIdentifier.getPortIdentifiers()
        .toList()
        .map { it.name }
        .sorted()
        .toTypedArray()