# Changelog

## 0.1.0-SNAPSHOT (YYYY-MM-DD)

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