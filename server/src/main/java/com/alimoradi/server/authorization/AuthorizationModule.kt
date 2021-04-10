package com.alimoradi.server.authorization

import com.alimoradi.server.main.ApplicationGraph

class AuthorizationModule {

    fun createAuthorizationManager(): AuthorizationManager {
        val logManager = ApplicationGraph.getLogManager()
        val fileOnlineAuthentications = ApplicationGraph.getFileOnlineAuthentications()
        return AuthorizationManagerImpl(
            logManager,
            fileOnlineAuthentications
        )
    }
}
