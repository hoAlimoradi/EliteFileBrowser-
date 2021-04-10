package com.alimoradi.feature_event_mercan

interface EventNetwork {

    fun postAsync(
        url: String,
        headers: Map<String, String>,
        body: String
    )
}
