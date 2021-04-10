package com.alimoradi.feature_event_mercan

interface EventMetadataLongInterceptor {

    fun getKey(): String

    fun getValue(): Long
}
