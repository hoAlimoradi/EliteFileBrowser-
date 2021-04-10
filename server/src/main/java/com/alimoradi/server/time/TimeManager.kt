package com.alimoradi.server.time

interface TimeManager {

    fun getDayString(): String

    fun getTimeString(): String

    fun getTimeFileNameString(): String

    fun getTimeMillis(): Long
}
