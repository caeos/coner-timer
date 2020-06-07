package org.coner.timer.cli

import com.github.ajalt.clikt.output.TermUi
import org.coner.timer.model.FinishTriggerElapsedTimeOnly
import org.coner.timer.output.TimerOutputWriter

class SimpleEchoMappedInputWriter : TimerOutputWriter<FinishTriggerElapsedTimeOnly> {
    override fun write(input: FinishTriggerElapsedTimeOnly) {
        TermUi.echo(input.et, trailingNewline = true)
    }
}