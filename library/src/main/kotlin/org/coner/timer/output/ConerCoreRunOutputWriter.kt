package org.coner.timer.output

import org.coner.core.client.api.EventsApi
import org.coner.core.client.model.AddRawTimeToFirstRunLackingRequest
import org.coner.timer.model.FinishTriggerElapsedTimeOnly

class ConerCoreRunOutputWriter(val api: EventsApi, val eventId: String) : TimerOutputWriter<FinishTriggerElapsedTimeOnly> {

    override fun write(input: FinishTriggerElapsedTimeOnly) {
        val body = AddRawTimeToFirstRunLackingRequest().apply {
            rawTime = input.et
        }
        api.addRawTimeToFirstRunInSequenceLackingOne(eventId, body)
    }

}