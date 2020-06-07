package org.coner.timer.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.output.TermUi
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import org.coner.timer.Timer

fun main(args: Array<String>) = ConerTimerCliCommand()
        .subcommands(
                FileCommand()
                        .subcommands(FileReplayCommand()),
                PortCommand()
                        .subcommands(PortListCommand(), PortCaptureCommand())
        )
        .main(args)

class ConerTimerCliCommand : CliktCommand() {
    override fun run() = Unit
}

fun <RTI, I> runTimer(timer: Timer<RTI, I>) {
    TermUi.echo("Starting timer... ", trailingNewline = false)
    timer.start()
    TermUi.echo("OK")
    TermUi.echo("Press Enter to stop...")
    TermUi.confirm("", abort = false, promptSuffix = "", showDefault = false, default = true)
    TermUi.echo("Stopping timer...", trailingNewline = false)
    timer.stop()
    TermUi.echo("OK")
}

fun CliktCommand.rawInputLogFileArgument() = option("--raw-log")
        .file(
                mustExist = false,
                canBeDir = false,
                mustBeWritable = true
        )
