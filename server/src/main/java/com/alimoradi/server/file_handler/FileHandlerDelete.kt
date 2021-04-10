@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.alimoradi.server.file_handler

import io.ktor.http.Headers

interface FileHandlerDelete {

    fun deleteFile(
        headers: Headers,
        body: String
    ): String
}
