package com.alimoradi.server.time

class TimeModule {

    fun createTimeManager(): TimeManager {
        return TimeManagerImpl()
    }
}