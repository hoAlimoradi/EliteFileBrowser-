package com.alimoradi.server.event

interface EventHandlerPost {

    fun handleEvent(
        platform: String,
        applicationPackageName: String,
        applicationVersionName: String,
        idAddress: String,
        body: String
    ): EventResponse
}
