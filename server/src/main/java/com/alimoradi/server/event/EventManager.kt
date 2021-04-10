package com.alimoradi.server.event

import com.alimoradi.feature_event_mercan.Event

interface EventManager {

    fun handle(
        platform: String,
        applicationPackageName: String,
        applicationVersionName: String,
        idAddress: String,
        events: List<Event>
    ): EventResponse

    fun get(
        platform: String,
        applicationPackageName: String,
        applicationVersionName: String
    ): List<String>
}
