package com.alimoradi.server.shell

import com.alimoradi.server.main.ApplicationGraph

class ShellModule {

    private val logManager by lazy { ApplicationGraph.getLogManager() }

    fun createShellManager(): ShellManager {
        return ShellManagerImpl(
            logManager
        )
    }
}
