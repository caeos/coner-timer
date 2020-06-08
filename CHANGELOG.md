# Changelog

## 0.1.2 (pending)

- Supports Java 11+
- Timer can stop when finding blank line to support log replay
- Improved CLI app

## 0.1.1 (2018-12-02)

Fix changelog, tagged release

## 0.1.0 (2018-12-02)

Initial release.

Features:
- Receive times:
    - From:
        - Hardware timers
        - Arbitrary input streams (intended for development/testing use only)
    - As:
        - JACircuits normal format
    - Raw input logged to files (replay with arbitrary input streams)
- Write times:
    - Users should implement `org.coner.timer.output.TimerOutputWriter`
    - Basic println plugin (intended for development/testing use only)