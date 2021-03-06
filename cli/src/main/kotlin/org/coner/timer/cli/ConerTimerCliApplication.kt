package org.coner.timer.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.output.TermUi
import com.github.ajalt.clikt.parameters.options.versionOption
import org.coner.timer.Timer

fun factory() = ConerTimerCliApplication()
        .subcommands(
                FileCommand()
                        .subcommands(FileReplayCommand()),
                PortCommand()
                        .subcommands(PortListCommand(), PortCaptureCommand())
        )

fun main(args: Array<String>) = factory().main(args)

class ConerTimerCliApplication : CliktCommand(name = "coner-timer-cli") {

    init {
        versionOption("${javaClass.`package`?.implementationVersion}")
    }

    override fun run() = Unit
}

fun <RTI, I> runTimer(
        timer: Timer<RTI, I>,
        confirmToStop: Boolean = true,
        verbose: Boolean = false
) {
    if (verbose) TermUi.echo("Starting timer... ", trailingNewline = false)
    timer.start()
    if (verbose) TermUi.echo("OK")
    if (confirmToStop) {
        TermUi.echo("Press Enter to stop...")
        TermUi.confirm("", abort = false, promptSuffix = "", showDefault = false, default = true)
        if (verbose) TermUi.echo("Stopping timer...", trailingNewline = false)
        timer.stop()
        if (verbose) TermUi.echo("OK")
    }
}
