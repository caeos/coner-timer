package org.coner.timer.util

import com.fazecast.jSerialComm.SerialPort

class JSerialCommWrapper {

    fun getSerialPorts() = SerialPort.getCommPorts()
}