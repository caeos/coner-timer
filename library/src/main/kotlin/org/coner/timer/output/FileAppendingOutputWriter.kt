package org.coner.timer.output

import java.io.File

class FileAppendingOutputWriter(val file: File, val appendLineSeparator: Boolean = true) : TimerOutputWriter<String> {

    override fun write(input: String) {
        file.appendText(input)
        if (appendLineSeparator) {
            file.appendText(System.lineSeparator())
        }
    }
}