package com.alimoradi.server.authorization

import com.alimoradi.file_api_online.FileOnlineAuthentication
import com.alimoradi.server.log.LogManager
import io.ktor.http.Headers

internal class AuthorizationManagerImpl(
    private val logManager: LogManager,
    private val fileOnlineAuthentications: List<FileOnlineAuthentication>
) : AuthorizationManager {

    override fun isAuthorized(headers: Headers): Boolean {
        val authorization = headers["Authorization"]
        return if (authorization == null) {
            loge("Not authorized")
            false
        } else {
            logd("Authorized")
            val token = authorization.replace("Basic ", "")
            FileOnlineAuthentication.isLogged(token, fileOnlineAuthentications)
        }
    }

    private fun logd(message: String) {
        logManager.d(TAG, message)
    }

    private fun loge(message: String) {
        logManager.e(TAG, message)
    }

    companion object {

        private const val TAG = "AuthorizationManager"
    }
}
