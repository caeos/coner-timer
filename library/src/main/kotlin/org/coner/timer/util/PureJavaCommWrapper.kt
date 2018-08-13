package org.coner.timer.util

import purejavacomm.CommPortIdentifier

class PureJavaCommWrapper {

    fun getCommPortIdentifier(portName: String) = CommPortIdentifier.getPortIdentifier(portName)
}