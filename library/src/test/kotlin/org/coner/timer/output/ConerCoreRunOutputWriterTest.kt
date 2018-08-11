package org.coner.timer.output

import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.coner.core.client.api.EventsApi
import org.coner.core.client.model.AddRawTimeToFirstRunLackingRequest
import org.coner.timer.model.FinishTriggerElapsedTimeOnly
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class ConerCoreRunOutputWriterTest {

    lateinit var writer: ConerCoreRunOutputWriter

    lateinit var api: EventsApi
    val eventId = "EVENT-ID"

    @Before
    fun before() {
        api = mockk(relaxed = true)

        writer = ConerCoreRunOutputWriter(api, eventId)
    }

    @Test
    fun itShouldWriteTimeToEventsApi() {
        val input = FinishTriggerElapsedTimeOnly(BigDecimal.valueOf(123456, 3))

        writer.write(input)

        val bodySlot = slot<AddRawTimeToFirstRunLackingRequest>()
        verify { api.addRawTimeToFirstRunInSequenceLackingOne(eventId, capture(bodySlot)) }
        assertThat(bodySlot.captured).isNotNull
        assertThat(bodySlot.captured.rawTime).isEqualByComparingTo("123.456")
    }
}