package org.coner.timer.input.reader

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown
import org.junit.Before
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

class TimerInputReaderControllerTest {

    private lateinit var controller: TimerInputReaderController<Any>

    private lateinit var started: AtomicBoolean
    private lateinit var consumed: AtomicBoolean
    private lateinit var reader: TimerInputReader<Any>

    @Before
    fun before() {
        started = AtomicBoolean(false)
        consumed = AtomicBoolean(false)
        reader = mockk(relaxed = true)
        controller = TimerInputReaderController(started, consumed, reader)
    }

    @Test
    fun whenStartFromStoppedItShouldFlagStarted() {
        controller.start()

        assertThat(started).isTrue
        verify { reader.onStart() }
    }

    @Test
    fun whenStartFromStartedItShouldThrow() {
        started.set(true)

        try {
            controller.start()
            failBecauseExceptionWasNotThrown(IllegalArgumentException::class.java)
        } catch (t: Throwable) {
            assertThat(t).isInstanceOf(IllegalArgumentException::class.java)
        }
    }

    @Test
    fun whenStartFromConsumedItShouldThrow() {
        consumed.set(true)

        try {
            controller.start()
            failBecauseExceptionWasNotThrown(IllegalArgumentException::class.java)
        } catch (t: Throwable) {
            assertThat(t).isInstanceOf(IllegalArgumentException::class.java)
        }
    }

    @Test
    fun whenReadFromStartedItShouldRead() {
        started.set(true)
        every { reader.read() }.returns("foo")

        val actual = controller.read()

        assertThat(actual).isEqualTo("foo")
        verify { reader.read() }
    }

    @Test
    fun whenReadFromStoppedItShouldThrow() {
        try {
            controller.read()
            failBecauseExceptionWasNotThrown(IllegalArgumentException::class.java)
        } catch (t: Throwable) {
            assertThat(t).isInstanceOf(IllegalArgumentException::class.java)
        }
        verify(exactly = 0) { reader.read() }
    }

    @Test
    fun whenStopFromStartedItShouldStop() {
        started.set(true)

        controller.stop()

        assertThat(consumed).isTrue
        assertThat(started).isFalse
        verify { reader.onStop() }
    }

    @Test
    fun whenStopFromStoppedItShouldThrow() {
        try {
            controller.stop()
            failBecauseExceptionWasNotThrown(IllegalArgumentException::class.java)
        } catch (t: Throwable) {
            assertThat(t).isInstanceOf(IllegalArgumentException::class.java)
        }
    }
}
