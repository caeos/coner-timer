# coner-timer

A library for interfacing with hardware timers

[![Build Status](https://travis-ci.org/caeos/coner-timer.svg?branch=master)](https://travis-ci.org/caeos/coner-timer)
[![codecov](https://codecov.io/gh/caeos/coner-timer/branch/master/graph/badge.svg)](https://codecov.io/gh/caeos/coner-timer)

## Library

The library provides the tools your application needs to capture times from hardware timers.

- Capture times from hardware timers connected by USB/serial port
- Supports JACircuits encoded times

### Usage:

Maven dependency:
```xml
<dependency>
    <groupId>org.coner</groupId>
    <artifactId>coner-timer-library</artifactId>
</dependency>
```

Start logging times:
```kotlin
val reader = PureJavaCommTimerInputReader(
     pureJavaComm = PureJavaCommWrapper(),
     appName = "coner-timer-cli",
     port = "ttyUSB0"
)
val timer = Timer(
    controller = TimerInputReaderController(reader = reader),
    rawInputWriter = FileAppendingOutputWriter(rawInputLogFile),
    mapper = JACTimerInputMapper(),
    mappedInputWriter = PrintlnTimerOutputWriter<FinishTriggerElapsedTimeOnly>(),
)
timer.start()
// receive some times on your hardware timer connected to ttyUSB0

// when you're ready to stop receiving times
timer.stop()
```

## CLI

A functional reference app demonstrating time capture with the Coner Timer library.

### Build and run

Prerequisites:
- JDK 8

Produce a build:
```shell script
./mvnw clean package
```

List available ports:
```shell script
java -jar cli/target/coner-timer-cli-*.jar port list
```

Capture times from hardware timer on ttyUSB0, log raw input to `raw-input.log`, and echo formatted times to console:
```shell script
java -jar cli/target/coner-timer-cli-*.jar port capture --raw-log raw-input.log ttyUSB0
```

Replay times in `raw-input.log` as input, and echo formatted times to console:
```shell script
java -jar cli/target/coner-timer-cli-*.jar file replay raw-input.log
```