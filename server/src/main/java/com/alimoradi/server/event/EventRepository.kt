package com.alimoradi.server.event

import com.alimoradi.feature_event_mercan.Event

interface EventRepository {

    fun put(
        platform: String,
        applicationPackageName: String,
        applicationVersionName: String,
        events: List<Event>
    ): EventResponse

    fun get(
        platform: String,
        applicationPackageName: String,
        applicationVersionName: String
    ): List<Event>
}
