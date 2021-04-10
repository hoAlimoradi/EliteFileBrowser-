package com.alimoradi.server.authorization

import io.ktor.http.Headers

interface AuthorizationManager {

    fun isAuthorized(headers: Headers): Boolean
}
