package com.alimoradi.feature_event_mercan

interface EventReceiver {

    fun receive(requestBody: String): List<Event>
}
