package com.alimoradi.server.log

import com.alimoradi.server.main.ApplicationGraph

class LogModule {

    private val timeManager by lazy { ApplicationGraph.getTimeManager() }

    fun createLogManager(): LogManager {
        val rootPath = ApplicationGraph.getRootPath()
        return LogManagerImpl(
            rootPath,
            timeManager
        )
    }
}
