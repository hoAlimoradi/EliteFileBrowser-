package com.alimoradi.feature_event_mercan

interface EventSender {

    fun send(key: String, value: String)

    fun addEventMetadataBooleanInterceptor(interceptor: EventMetadataBooleanInterceptor)

    fun addEventMetadataLongInterceptor(interceptor: EventMetadataLongInterceptor)

    fun addEventMetadataStringInterceptor(interceptor: EventMetadataStringInterceptor)
}
