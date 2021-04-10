package com.alimoradi.feature_event_mercan

class EventMetadataBooleanInterceptorStatic(
    private val key: String,
    private val value: Boolean
) : EventMetadataBooleanInterceptor {

    override fun getKey(): String {
        return key
    }

    override fun getValue(): Boolean {
        return value
    }
}
