@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.alimoradi.server.file_repository

import com.alimoradi.file_api.File

interface FileRepository {

    fun getFolderContainerPath(): String

    fun has(path: String): Boolean

    fun get(): List<File>

    fun get(path: String): File

    fun getChildren(parentPath: String): List<File>

    fun put(file: File)

    fun delete(path: String): File?

    fun rename(path: String, name: String): File?

    fun copy(path: String, pathDirectoryOutput: String): File?

    fun cut(path: String, pathDirectoryOutput: String): File?
}
