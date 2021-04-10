package com.alimoradi.feature_event_mercan

interface EventSecure {

    fun crypt(message: String): String

    fun decrypt(message: String): String
}
