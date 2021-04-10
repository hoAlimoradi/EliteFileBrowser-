package com.alimoradi.feature_event_mercan

class EventModule(
    private val eventSecure: EventSecure
) {

    fun createEventSender(
        url: String,
        eventNetwork: EventNetwork
    ): EventSender {
        return EventSenderImpl(
            url,
            eventNetwork,
            eventSecure
        )
    }

    fun createEventReceiver(): EventReceiver {
        return EventReceiverImpl(
            eventSecure
        )
    }
}
